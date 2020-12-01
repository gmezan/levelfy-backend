package com.uc.backend.controller.asesor;

import com.uc.backend.entity.*;
import com.uc.backend.job.ClaseDeactivationJob;
import com.uc.backend.repository.*;
import com.uc.backend.service.CustomEmailService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

import static com.uc.backend.utils.CustomConstants.*;


@Controller
@RequestMapping("a/ases-paq")
public class CrudAsesPaqController {
    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    VentaCanceladaRepository ventaCanceladaRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;
    @Autowired
    CustomEmailService customEmailService;
    @Autowired
    private Scheduler scheduler;


    @GetMapping("info/{id}")
    public String infoAsesPaq(@PathVariable("id") int id, Model model,
                              HttpSession session, RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> optionalClase =
                claseRepository.findByIdclaseAndProfesor_IdusuarioAndServicio(id,user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE);
        if (optionalClase.isPresent()){
            model.addAttribute("clase", optionalClase.get());

            return "asesor/ases-paq/infoAsesPaq";
        }else {
            attributes.addFlashAttribute("msgError","Ocurrió un problema");
            return "redirect:/a/ases-paq/list";
        }
    }

    // ---------------------------------------- METODOS PARA EL CRUD -------------------------------------------------

    /*
    @GetMapping("edit/{id}")
    public String editAsesPaq(@PathVariable("id") int id, Model model,
                              HttpSession session, RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        return "asesor/ases-paq/formAsesPaq";
    }*/

    @GetMapping("new")
    public String formAsesPaq(@ModelAttribute("clase") Clase clase, HttpSession session, Model model){
        Usuario user = (Usuario)session.getAttribute("usuario");
        clase = new Clase();
        clase.setCurso(new Curso(user.getUniversidad()));
        clase.setVencimiento(LocalDate.now().plusMonths(1));
        clase.setClaseSesions(new ArrayList<>());
        clase.setPrecio(BigDecimal.valueOf(PRECIO_BASE_ASES_PAQ_PUCP));
        clase.setProfesor(user);
        for (int i=0; i<5;i++) clase.getClaseSesions().add(new ClaseSesion(clase));
        model.addAttribute("title","Nuevo Paquete de Asesorías");
        model.addAttribute("clase",clase);
        model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
        model.addAttribute("listaEvaluaciones", EVALUACION);
        return "asesor/ases-paq/formAsesPaq";
    }

    @GetMapping("list")
    public String listAsesPaq(Model model, HttpSession session){
        Usuario user = (Usuario)session.getAttribute("usuario");
        model.addAttribute("listAsesPaq", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE));
        model.addAttribute("listAsesPaqPas", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsFalseAndServicio(user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE));
        return "asesor/ases-paq/listAsesPaq";
    }

    // Para editar y nuevo
    @PostMapping("save")
    public String saveAsesPaq(@ModelAttribute("clase") @Valid Clase clase, BindingResult bindingResult,
                              HttpSession session, Model model, RedirectAttributes attributes) throws SchedulerException {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ClaseSesion claseSesion;

        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Curso> optionalCurso = cursoRepository.findById(clase.getCurso().getCursoId());

        if (!EVALUACION.containsKey(clase.getEvaluacion()))
            bindingResult.rejectValue("evaluacion","error.user","Elija la evaluación");

        if (clase.getNumSesiones()<0 || clase.getNumSesiones()>5)
            bindingResult.rejectValue("numSesiones","error.user", "Elija el número de sesiones");

        if (!UNIVERSIDAD.containsKey(clase.getCurso().getCursoId().getUniversidad()) ||
                !(user.getUniversidad().equals(clase.getCurso().getCursoId().getUniversidad())))
            bindingResult.rejectValue("curso.cursoId.universidad","error.user", "Elija la universidad");

        if (!optionalCurso.isPresent())
            bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Elija el curso");

        if (clase.getIdclase()==0){//Si es nuevo

            Optional<Clase> optionalClase = claseRepository.findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndEvaluacionAndDisponibleIsTrue
                    (user.getIdusuario(), SERVICIO_ASESORIA_PAQUETE, clase.getCurso().getCursoId(), clase.getEvaluacion());

            if (optionalClase.isPresent())
                bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Ya existe una asesoría para este curso");


            // VALIDACION DE LAS SESIONES DE LA ASESORIA:
            if (!bindingResult.hasErrors()){
                for (int i = 0; i < clase.getNumSesiones(); i++){
                    claseSesion = clase.getClaseSesions().get(i);

                    //Se valida la fecha
                    if (claseSesion.getFecha()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].fecha","error.user", "Ingrese una fecha");
                    else if (LocalDateTime.now().minusHours(5).plusDays(1).toLocalDate().isAfter(claseSesion.getFecha()) || LocalDate.now().plusMonths(2).isBefore(claseSesion.getFecha()))
                        bindingResult.rejectValue("claseSesions["+i+"].fecha","error.user", "Ingrese una fecha válida");

                    //Se valida la hora de inicio y fin
                    if (claseSesion.getInicio()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].inicio","error.user", "Ingrese un inicio");
                    else if (LocalTime.of(6,0).isAfter(claseSesion.getInicio()) || LocalTime.of(23, 1).isBefore(claseSesion.getInicio()))
                        bindingResult.rejectValue("claseSesions["+i+"].inicio","error.user", "Ingrese un hora de inicio válida: De 6:00 a 23:00");
                    else if (claseSesion.getFin()!=null)
                    {
                        if (claseSesion.getFin().isBefore(claseSesion.getInicio()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "Ingrese una hora fin válida");

                        if (claseSesion.getInicio().plusMinutes(30).isAfter(claseSesion.getFin()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "La sesión no puede durar menos de 30 minutos");

                        if (claseSesion.getInicio().isBefore(LocalTime.of(9,0)) && claseSesion.getInicio().plusHours(3).isBefore(claseSesion.getFin()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "La sesión no puede durar más de 3 horas");
                        if (claseSesion.getZoom().isEmpty() )
                            bindingResult.rejectValue("claseSesions["+i+"].zoom","error.user", "La sesión debe tener un enlace de zoom o meets");

                    }
                    if (claseSesion.getFin()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "Ingrese un fin");
                }
            }

            if (bindingResult.hasErrors()){
                clase.setFoto(clase.getCurso().getFoto());
                clase.setProfesor(user);
                clase.getCurso().getCursoId().setUniversidad(user.getUniversidad());
                model.addAttribute("title","Nuevo Paquete de Asesoría");
                model.addAttribute("clase",clase);
                model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
                model.addAttribute("listaEvaluaciones", EVALUACION);
                return "asesor/ases-paq/formAsesPaq";
            }
            //Si all está bien
            else {
                //TODO: schedule un job que desactive la asesoría luego de la última clase
                List<ClaseSesion> claseSesionList = new ArrayList<ClaseSesion>(){{
                    for (int i = 0; i < clase.getNumSesiones(); i++)
                        add(new ClaseSesion(clase, clase.getClaseSesions().get(i)));
                }};
                clase.setClaseSesions(null);
                clase.setServicio(SERVICIO_ASESORIA_PAQUETE);
                clase.setProfesor(user);
                clase.setCurso(optionalCurso.orElse(null));
                clase.setFoto(clase.getCurso().getFoto());
                clase.setDisponible(true);
                claseRepository.save(clase);
                claseSesionList.forEach(claseSesion1 -> claseSesion1.setClase(clase));
                claseSesionRepository.saveAll(claseSesionList);
                JobDetail jobDetail = buildAvailabilityDeactivationJobDetail(clase);
                List<LocalDate> localDates = new ArrayList<LocalDate>(){{
                   claseSesionList.forEach(claseSesion1 -> add(claseSesion1.getFecha()));
                }};
                Collections.sort(localDates);
                scheduler.scheduleJob(jobDetail, buildAvailabilityDeactivationJobTrigger(jobDetail,
                        ZonedDateTime.of(localDates.get(localDates.size()-1),LocalTime.of(23,59), zonedDateTime.getZone())));
                attributes.addFlashAttribute("msgSuccess","Paquete de asesorías creado correctamente");
                return "redirect:/a/ases-paq/list";
            }
        }
        return "redirect:/a/ases-paq/list";
    }
//borrar ases-paq
    @PostMapping("ases-paq/delete")
    public String deleteAsesPaq(@RequestParam("idclase") Integer id, HttpSession session, RedirectAttributes attributes) throws IOException, MessagingException {
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> claseOptional = claseRepository.
                findByIdclaseAndProfesor_IdusuarioAndServicioAndDisponibleIsTrue(id, user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE);
        if (claseOptional.isPresent()){
            Clase clase = claseOptional.get();

            List<Venta> ventasDeClase =  ventaRepository.findVentasByClaseEnroll_Clase(clase);

            if (!ventasDeClase.isEmpty()){
                //TODO: notificar a los usuarios via correo que la clase fue cancelada
                List<Usuario> listaInscritos = new ArrayList<>() ;

                List<ClaseEnroll> listaClaseEnroll = claseEnrollRepository.findClaseEnrollByClase_Idclase(clase.getIdclase());
                for (ClaseEnroll c: listaClaseEnroll){
                    Usuario u=c.getEstudiante();
                    listaInscritos.add(u);
                }
                String mensaje="Lamentamos informarles que la clase ha sido cancelada por motivos que escapan de " +
                        "manos de la academia.Esperamos su comprensión. Nos pondremos en contacto a la brevedad para reponer el dinero en caso se halla realizado el pago." +
                        "Gracias por su comprensión, esperamos contar con su preferencia siempre.";
                customEmailService.sendMailToUsersMensaje(listaInscritos,listaClaseEnroll.get(0).getClase(),mensaje);
                ventaCanceladaRepository.saveAll(VentaCancelada.generateVentaCanceladas(ventasDeClase));
                ventaRepository.deleteAll(ventasDeClase);
            }
            claseSesionRepository.deleteAll(clase.getClaseSesions());
            claseEnrollRepository.deleteAll(clase.getClaseEnrollList());
            claseRepository.delete(clase);
            attributes.addFlashAttribute("msgSuccess", "Paquete de Asesorías eliminado correctamente");

        }else {
            attributes.addFlashAttribute("msgError", "No se encontró la clase");
        }
        return "redirect:/a/ases-paq/list";
    }


    private JobDetail buildAvailabilityDeactivationJobDetail(Clase clase) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("idclase", clase.getIdclase());

        return JobBuilder.newJob(ClaseDeactivationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "a-ases-per-jobs")
                .withDescription("ASES_PER Availability expiration Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildAvailabilityDeactivationJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "a-ases-per-triggers")
                .withDescription("Expiration for ASES_PER availability")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }


}
