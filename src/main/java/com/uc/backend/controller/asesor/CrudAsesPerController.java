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

import static com.uc.backend.utils.CustomConstants.SERVICIO_ASESORIA_PERSONALIZADA;
import static com.uc.backend.utils.CustomConstants.*;

@Controller
@RequestMapping("a/ases-per")
public class CrudAsesPerController {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    VentaRepository ventaRepository;
@Autowired
    CustomEmailService customEmailService;
    @Autowired
    VentaCanceladaRepository ventaCanceladaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired private Scheduler scheduler;

    /*
        Para que se cree la clase se debe de tener:
        1. idprofesor: se obtiene con HttpSession
        2. idcurso: ModelAttribute
        3. universidad: La unversidad del asesor
        4. foto: foto del curso ?
        5. servicio: se autocompleta por cada servicio
        6. evaluacion: solo para ases-paq
        7. paquete: se autocompleta

        8. Se debe pedir la fecha de vencimiento para el caso de las clases self-p y ases-per
     */

    @GetMapping("info/{id}")
    public String infoAsesPer(@PathVariable("id") int id, Model model,
                              HttpSession session, RedirectAttributes attributes){
        Usuario user = usuarioRepository.findById(((Usuario)session.getAttribute("usuario")).getIdusuario()).orElse(null);

        if(user!=null){
            Optional<Clase> optionalClase =
                    claseRepository.findByIdclaseAndProfesor_IdusuarioAndServicio(id,user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA);
            if (optionalClase.isPresent()){
                model.addAttribute("clase", optionalClase.get());
                return "asesor/ases-per/infoAsesPer";
            }else
                attributes.addFlashAttribute("msgError","Ocurrió un problema");

        }
        return "redirect:/a/ases-per/list";
    }

    @GetMapping("toggle/{id}")
    public String toggleAsesPer(@PathVariable("id") int id,
                              HttpSession session, RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> claseOptional = claseRepository.findByIdclaseAndProfesor_IdusuarioAndServicioAndArchivedIsFalse
                (id, user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA);
        if (claseOptional.isPresent()){
            Clase clase = claseOptional.get();
            if(clase.getDisponible()){
                clase.setDisponible(false);
                attributes.addFlashAttribute("msgSuccess", "Operación exitosa, la clase ya no está disponible ");
            }else {
                clase.setDisponible(true);
                attributes.addFlashAttribute("msgSuccess", "Operación exitosa, la clase está disponible");
            }
            claseRepository.save(clase);
        }else {
            attributes.addFlashAttribute("msgError", "No se encontró la clase");
        }
        return "redirect:/a/ases-per/list";

    }

    @PostMapping("cancelEnroll")
    public String cancelEnrollAsesPer(@RequestParam("idclase") Integer idclase,@RequestParam("idClaseEnroll") Integer idClaseEnroll,
                                      HttpSession session, RedirectAttributes attributes) throws IOException, MessagingException {
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndProfesor_IdusuarioAndServicioAndArchivedIsFalse(idclase,
                user.getIdusuario(), SERVICIO_ASESORIA_PERSONALIZADA);

        if (optionalClase.isPresent()){
            Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                    findClaseEnrollByActiveIsTrueAndClase_Profesor_IdusuarioAndIdClaseEnrollAndClase_Idclase(
                            user.getIdusuario(), idClaseEnroll, idclase);

            if (optionalClaseEnroll.isPresent()){ //La claseEnroll si existe y está activa
                ClaseEnroll claseEnroll = optionalClaseEnroll.get();
                if (!claseEnroll.getVentaList().isEmpty()){
                    ventaCanceladaRepository.saveAll(VentaCancelada.generateVentaCanceladas(claseEnroll.getVentaList()));
                    ventaRepository.deleteAll(claseEnroll.getVentaList());
                    // TODO: notificar al usuario que su clase fue cancelada
                    List<Usuario> listaInscritos = new ArrayList<>() ;

                    List<ClaseEnroll> listaClaseEnroll = claseEnrollRepository.findClaseEnrollByClase_Idclase(claseEnroll.getClase().getIdclase());
                    for (ClaseEnroll c: listaClaseEnroll){
                        Usuario u=c.getEstudiante();
                        listaInscritos.add(u);
                    }
                    String mensaje="Lamentamos informarles que la clase ha sido cancelada por motivos que escapan de " +
                            "manos de la academia.Esperamos su comprensión. Nos pondremos en contacto a la brevedad para reponer el dinero en caso se halla realizado el pago." +
                            "Gracias por su comprensión, esperamos contar con su preferencia siempre.";
                    customEmailService.sendMailToUsersMensaje(listaInscritos,listaClaseEnroll.get(0).getClase(),mensaje);
                }

                claseEnrollRepository.delete(claseEnroll);
                attributes.addFlashAttribute("msgSuccess", "Inscripción cancelada correctamente");
            }else {
                attributes.addFlashAttribute("msgError", "No se pudo encontrar la inscripción");
            }
            return "redirect:/a/ases-per/info/"+idclase;
        }else {
            attributes.addFlashAttribute("msgError","Ocurrió un problema");
            return "redirect:/a/ases-per/list";
        }

    }

    @PostMapping("deactivate")
    public String deactivateAsesPer(@RequestParam("idclase") Integer idclase,
                                      HttpSession session, RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndProfesor_IdusuarioAndServicioAndArchivedIsFalse(idclase,
                user.getIdusuario(), SERVICIO_ASESORIA_PERSONALIZADA);
        if (optionalClase.isPresent()){
            Clase clase = optionalClase.get();
            if (clase.getClaseEnrollActiveList().isEmpty()){
                clase.setArchived(Boolean.TRUE);
                clase.setDisponible(Boolean.FALSE);
                claseRepository.save(clase);
                attributes.addFlashAttribute("msgSuccess", "Asesoría Personalizada desactivada correctamente");
            }
            else {
                attributes.addFlashAttribute("msgError", "Esta Asesoría Personalizada no se puede desactivar porque hay inscripciones pendientes");
            }
            return "redirect:/a/ases-per/info/"+idclase;

        }else {
            attributes.addFlashAttribute("msgError","Ocurrió un problema");
            return "redirect:/a/ases-per/list";
        }
    }



    // ---------------------------------------- METODOS PARA EL CRUD -------------------------------------------------

    /*
    @GetMapping("edit/{id}")
    public String editAsesPer(@PathVariable("id") int id, @ModelAttribute("clase") Clase clase, Model model,
                              HttpSession session, RedirectAttributes attributes){

        return "asesor/ases-per/formAsesPer";
    }*/

    @GetMapping("new")
    public String formAsesPer(@ModelAttribute("clase") Clase clase, HttpSession session, Model model){
        Usuario user = usuarioRepository.findById(((Usuario)session.getAttribute("usuario")).getIdusuario()).orElse(null);
        if (user!=null){ //Esto se hace para que se actualice la disponibilidad de inmediato (la lista de disp)
            clase = new Clase();
            clase.setCurso(new Curso(user.getUniversidad()));
            clase.setVencimiento(LocalDate.now().plusMonths(1));
            clase.setProfesor(user);
            clase.setPrecio(BigDecimal.valueOf(PRECIO_BASE_ASES_PER_PUCP));
            model.addAttribute("title","Nueva Asesoría Personalizada");
            model.addAttribute("clase",clase);
            model.addAttribute("availability", user.getDisponibilidadProfesorList());
            model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
            return "asesor/ases-per/formAsesPer";
        }
        return "redirect:/a/ases-per/list";
    }

    @GetMapping("list") //Listado general
    public String listAsesPer(Model model, HttpSession session){
        Usuario user = (Usuario)session.getAttribute("usuario");
        model.addAttribute("listAsesPer", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA));
        model.addAttribute("listAsesPerPas", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsFalseAndServicio(user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA));
        return "asesor/ases-per/listAsesPer";
    }

    // Para editar y nuevo
    @PostMapping("save")
    public String saveAsesPer(@ModelAttribute("clase") @Valid Clase clase, BindingResult bindingResult, HttpSession session,
                              Model model, RedirectAttributes attributes) throws SchedulerException {
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Curso> optionalCurso = cursoRepository.findById(clase.getCurso().getCursoId());

        if (!UNIVERSIDAD.containsKey(clase.getCurso().getCursoId().getUniversidad()) ||
                !(user.getUniversidad().equals(clase.getCurso().getCursoId().getUniversidad())))
            bindingResult.rejectValue("curso.cursoId.universidad","error.user", "Elija la universidad");

        if (!optionalCurso.isPresent())
            bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Elija el curso");

        if (clase.getVencimiento()==null || clase.getVencimiento().isBefore(LocalDateTime.now().minusHours(5).toLocalDate()))
            bindingResult.rejectValue("vencimiento","error.user", "Elija la fecha límite");

        if (clase.getIdclase()==0){//Si es nuevo

            Optional<Clase> optionalClase = claseRepository.findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndArchivedIsFalse
                    (user.getIdusuario(), SERVICIO_ASESORIA_PERSONALIZADA, clase.getCurso().getCursoId());

            if (optionalClase.isPresent())
                bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Ya existe una asesoría para este curso");

            if (bindingResult.hasErrors()){
                clase.setProfesor(user);
                model.addAttribute("title","Nueva Asesoría Personalizada");
                model.addAttribute("clase",clase);
                model.addAttribute("availability", user.getDisponibilidadProfesorList());
                model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
                return "asesor/ases-per/formAsesPer";
            }
            else {
                clase.setDisponible(Boolean.TRUE);
                clase.setArchived(Boolean.FALSE);
                clase.setCurso(optionalCurso.orElse(null));
                clase.setFoto(user.getFoto());
                clase.setServicio(SERVICIO_ASESORIA_PERSONALIZADA);
                claseRepository.save(clase);
                JobDetail jobDetail = buildAvailabilityDeactivationJobDetail(clase);
                scheduler.scheduleJob(jobDetail, buildAvailabilityDeactivationJobTrigger(jobDetail,
                        ZonedDateTime.of(LocalDateTime.of(clase.getVencimiento(), LocalTime.of(23,59)),ZonedDateTime.now().getZone())));
                attributes.addFlashAttribute("msgSuccess","Asesoría Personalizada creada correctamente");
            }

        }else {
            Optional<Clase> optionalClase =
                    claseRepository.findByIdclaseAndProfesor_IdusuarioAndServicioAndDisponibleIsTrue(clase.getIdclase(), user.getIdusuario(), SERVICIO_ASESORIA_PERSONALIZADA);
            attributes.addFlashAttribute("msgSuccess","Asesoría Personalizada actualizada correctamente");
        }
        return "redirect:/a/ases-per/list";
    }

    //No se debería borrar una clase archivada
    @GetMapping("delete/{id}")
    public String deleteAsesPer(@PathVariable("id") int id, HttpSession session, RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> claseOptional = claseRepository.
                findByIdclaseAndProfesor_IdusuarioAndServicioAndArchivedIsFalse(id, user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA);
        if (claseOptional.isPresent()){
            Clase clase = claseOptional.get();
            if(clase.getClaseEnrollList().isEmpty()){
                claseRepository.delete(clase);
                attributes.addFlashAttribute("msgSuccess", "Asesoría Personalizada de '"+clase.getCurso().getNombre()+"' borrada correctamente");
            }else {
                attributes.addFlashAttribute("msgError", "No se puede borrar esta clase debido a que" +
                        " hay inscritos. Si ya no se usará esta asesoría, se recomienda desactivarla.");
                return "redirect:/a/ases-per/info/"+id;
            }
        }else {
            attributes.addFlashAttribute("msgError", "No se encontró la clase");
        }
        return "redirect:/a/ases-per/list";
    }



    // ---------------------------------------------- For Jobs --------------------------------------------------

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
