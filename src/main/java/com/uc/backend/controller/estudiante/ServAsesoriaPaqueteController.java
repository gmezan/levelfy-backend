package com.uc.backend.controller.estudiante;

import com.uc.backend.dto.RegistroPagoDto;
import com.uc.backend.entity.*;
import com.uc.backend.job.asesper.AsesPersEnrollDeactivationJob;
import com.uc.backend.job.asesper.AsesPersEnrollExpirationJob;
import com.uc.backend.repository.*;
import com.uc.backend.service.CustomEmailService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.uc.backend.utils.CustomConstants.*;

@Controller
@RequestMapping("/service/ases-paq")
public class ServAsesoriaPaqueteController {
    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DisponibilidadProfesorRepository disponibilidadProfesorRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;

    @Autowired
    ComentarioForoRepository comentarioForoRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    CustomEmailService customEmailService;

    @Autowired
    VentaCanceladaRepository ventaCanceladaRepository;

    @Autowired
    private Scheduler scheduler;


    @PostMapping("/enroll")
    public String curso_processPaqueteAsesoria(@ModelAttribute("paqueteAsesoria") ClaseEnroll claseEnroll, HttpSession session,
                                               RedirectAttributes attributes) throws SchedulerException {

        ZonedDateTime dateTime = ZonedDateTime.now();

        Optional<Clase> claseOptional =
                claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(claseEnroll.getClase().getIdclase(),SERVICIO_ASESORIA_PAQUETE);

        if (claseOptional.isPresent()) {
            ClaseSesion cs = claseSesionRepository.findByClase_IdclaseOrderByFechaDescInicioDesc(claseEnroll.getClase().getIdclase()).get(0);
            LocalDateTime lastSession = LocalDateTime.of(cs.getFecha(), cs.getFin());
            Usuario user = (Usuario) session.getAttribute("usuario");
            claseEnroll.setCantidad(1);
            claseEnroll.setEstudiante(user);
            claseEnroll.setClase(claseOptional.get());
            claseEnroll.setPagado(false);
            claseEnroll.setActive(true);
            claseEnrollRepository.save(claseEnroll);
            JobDetail jobDetail = buildExpirationJobDetail(claseEnroll);
            JobDetail jobDetail2 = buildDeactivationJobDetail(claseEnroll);
            scheduler.scheduleJob(jobDetail, buildExpirationJobTrigger(jobDetail, dateTime.plusMinutes(MINUTES_EXPIRATION_TIME_FOR_ASES_PER)));
            //scheduler.scheduleJob(jobDetail2, buildDeactivationJobTrigger(jobDetail2, ZonedDateTime.of(lastSession.plusHours(6),dateTime.getZone())));
            attributes.addFlashAttribute("msgSuccess", "Gracias por inscribirte, recuerda realizar el pago en las siguientes "+(MINUTES_EXPIRATION_TIME_FOR_ASES_PER/60) + " horas, de lo contrario esta inscripción expirará.");
            return "redirect:/service/ases-paq/"+claseEnroll.getIdClaseEnroll();
        }
        attributes.addFlashAttribute("msgError", "Ocurrió un problema");
        return "redirect:/c/ases-paq";
    }


    @GetMapping("/{id}")
    public String confirmAsesoria(Model model, @PathVariable(name = "id") int idClaseEnroll,
                                  @ModelAttribute("paqueteAsesoria") ClaseEnroll claseEnroll,
                                  HttpSession session)
    {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio
                        (idClaseEnroll, user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE);

        if(optionalClaseEnroll.isPresent()){
            claseEnroll = optionalClaseEnroll.get();
            //Si hay un registro de pago
            ventaRepository.findVentaByClaseEnroll_IdClaseEnroll(claseEnroll.getIdClaseEnroll())
                    .ifPresent(venta -> model.addAttribute("registro",venta));

            model.addAttribute("paqueteAsesoria", claseEnroll);
            model.addAttribute("fechas", claseSesionRepository.findByClase_IdclaseOrderByFechaAscInicioAsc(
                    claseEnroll.getClase().getIdclase()));
            model.addAttribute("idUsuario",user.getIdusuario());

            return "estudiante/confirmPaqueteAsesoriaOnline";
        }
        return "redirect:/c/ases-paq";
    }

    @GetMapping("/cancel/{id}")
    public String cancelarAsesoria(HttpSession session,
                                   @PathVariable(name = "id") int idClaseEnroll,
                                   RedirectAttributes attributes) {

        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio
                        (idClaseEnroll, user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE);
        if (optionalClaseEnroll.isPresent()){

            ClaseEnroll claseEnroll = optionalClaseEnroll.get();

            if (!claseEnroll.getVentaList().isEmpty()){
                //Si es que pago, se guarda
                ventaCanceladaRepository.saveAll(VentaCancelada.generateVentaCanceladas(claseEnroll.getVentaList()));
                ventaRepository.deleteAll(claseEnroll.getVentaList());
                attributes.addFlashAttribute("msgSuccess", "Inscripción cancelada correctamente. Tenías un registro de pago, contáctanos para realizar la devolución.");
            }
            else {
                attributes.addFlashAttribute("msgSuccess", "Inscripción cancelada correctamente");
            }

            claseEnrollRepository.delete(claseEnroll);
        }
        else {
            attributes.addFlashAttribute("msgError", "Hubo un problema");
        }
        return "redirect:/u";
    }

    @PostMapping("/register")
    public String registroDePago(RegistroPagoDto registroPagoDto, HttpSession session, RedirectAttributes attributes) {
        Usuario user = (Usuario)session.getAttribute("usuario");

        Boolean flag = Boolean.FALSE;
        String msg = "";
        if (registroPagoDto.getIdclase()==null){
            msg="Hubo un problema con el registro, intenta de nuevo. Si el problema persiste, contáctanos";
            attributes.addFlashAttribute("msgError", msg);
            return "redirect:/u";
        }
        else if(registroPagoDto.getFecha()==null) // || registroPagoDto.getFecha().isBefore(LocalDateTime.now().minusHours(5).minusDays(2)))
           msg = "Hubo un problema al ingresar la fecha";
        else if (registroPagoDto.getPersona()==null || registroPagoDto.getPersona().isEmpty())
            msg = "Hubo un problema al ingresar el nombre de la persona que realizó el pago";
        else if (registroPagoDto.getCorreo()==null || !registroPagoDto.getCorreo().matches(".+@.+\\..+"))
            msg = "Hubo un problema al ingresar el correo electrónico";
        else if (registroPagoDto.getMonto()==null || !(registroPagoDto.getMonto().compareTo(BigDecimal.ZERO)>=0))
            msg = "Ingrese una cantidad válida";
        else if (!(METODOS_DE_PAGO.length>registroPagoDto.getMetodo() && registroPagoDto.getMetodo()>=0))
            msg = "Hubo un problema al ingresar el método de pago";
        else
            flag = Boolean.TRUE;

        if(flag) {
            Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                    findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio
                            (registroPagoDto.getIdclase(), user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE);
            if (optionalClaseEnroll.isPresent()) {
                Venta venta = ventaRepository.findVentaByClaseEnroll_IdClaseEnroll(optionalClaseEnroll.get().getIdClaseEnroll()).orElse(new Venta());
                venta.setMonto(registroPagoDto.getMonto());

                venta.setClaseEnroll(optionalClaseEnroll.get());
                venta.setFechapago(registroPagoDto.getFecha());
                venta.setPersona(registroPagoDto.getPersona());
                venta.setMensaje(registroPagoDto.getMensaje());
                venta.setMetodo(registroPagoDto.getMetodoStr());
                if(registroPagoDto.getCupon() != null && !registroPagoDto.getCupon().isEmpty()){
                    venta.setCupon(registroPagoDto.getCupon());
                }


                try {
                    ventaRepository.save(venta);
                    customEmailService.sendAsesPerRegisterMail(user, registroPagoDto);
                    attributes.addFlashAttribute("msgSuccess", "Registro de pago exitoso, confirmaremos el registro en breve");
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                    attributes.addFlashAttribute("msgError", "Hubo un problema con el registro, intenta de nuevo. Si el problema persiste, contáctanos");
                }
            }
        }
        else{
            attributes.addFlashAttribute("msgError", msg);
        }
        return "redirect:/service/ases-paq/"+registroPagoDto.getIdclase();
    }






    //Expiration (xhoras)
    private JobDetail buildExpirationJobDetail(ClaseEnroll claseEnroll) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("idClaseEnroll", claseEnroll.getIdClaseEnroll());

        return JobBuilder.newJob(AsesPersEnrollExpirationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "ases-paq-jobs")
                .withDescription("ASES_PAQ Inscription expiration Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
    private Trigger buildExpirationJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "ases-paq-triggers")
                .withDescription("Expiration for ASES_PAQ inscription")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }


    //Deactivation (despues de la clase)
    private JobDetail buildDeactivationJobDetail(ClaseEnroll claseEnroll) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("idClaseEnroll", claseEnroll.getIdClaseEnroll());

        return JobBuilder.newJob(AsesPersEnrollDeactivationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "ases-paq-deac-jobs")
                .withDescription("ASES_PAQ Inscription Deactivation Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
    private Trigger buildDeactivationJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "ases-paq-deac-triggers")
                .withDescription("Deactivation for ASES_PAQ inscription")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }


}
