package com.uc.backend.controller.admin;

import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import com.uc.backend.service.CustomEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.uc.backend.utils.CustomConstants.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
/*
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    CourseSuggestionRepository courseSuggestionRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentSessionRepository enrollmentSessionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SaleRepository saleRepository;

    @Autowired
    CustomEmailService customEmailService;
    @Autowired
    SaleCanceledRepository saleCanceledRepository;


    @GetMapping("/editarCurso")
    public String editarCurso(Model model){
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PERSONALIZADA);


        return "admin/editarCurso";
    }


    @GetMapping("/editarCurso/enroll/{id}")
    public String onlineCourseAsesoria(Model model, @ModelAttribute("asesoria") Service service, @PathVariable("id") int id, HttpSession session) {
        Optional<Service> optionalClase = serviceRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PERSONALIZADA);
        //List<Paquete> listaPaquetes = paqueteRepository.findAll();
        Map<LocalDateTime, String> disponibilidades;

        if (optionalClase.isPresent()) {

            service = optionalClase.get();
            model.addAttribute("asesoria", service);
            model.addAttribute("paquete", PAQUETES.get(1));
            return "cursos/formCursoAdmin"; //Envio a formulario de registro a Asesoria
        } else {
            return "redirect:/admin/editarCurso";
        } }

//asesor

    //Se muestran los cursos (disponibles) que está dictando el asesor


    @GetMapping(value = {"","/","/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model model) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        model.addAttribute("listaPaqueteAsesorias", lista);  //

        return "admin/curso/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = {"/ases-paq/lis-ins/{id}"})
    public String mostrarListaDeInscritos(Model model, @PathVariable("id") int id) {
        List<User> listaInscritos = new ArrayList<>() ;

        List<Enrollment> listaEnrollment = enrollmentRepository.findClaseEnrollByClase_Idclase(id);
        for (Enrollment c: listaEnrollment){
            User u=c.getStudent();
            listaInscritos.add(u);
        }
        model.addAttribute("idclase",id);
        model.addAttribute("listaInscritos", listaEnrollment);
        return "admin/listaInscritosAdmin";
    }


    @GetMapping(value = "/new/ases-paq")
    public String crearPaqueteAsesoria(Model model, Service service, HttpSession session){



        model.addAttribute("asesores", userRepository.findAllByRol_IdrolAndActivoIsTrue(2));
        List<String> universidad= courseRepository.findUniversidades();
        model.addAttribute("universidad",universidad);

        User user = (User) session.getAttribute("usuario");
        service = new Service();

        service.setExpiration(LocalDate.now().plusMonths(1));
        service.setClaseSesions(new ArrayList<>());
        service.setPrice(BigDecimal.valueOf(PRECIO_BASE_ASES_PAQ_PUCP));
        service.setTeacher(user);
        for (int i=0; i<5;i++) service.getClaseSesions().add(new EnrollmentSession(service));
        model.addAttribute("title","Nuevo Paquete de Asesorías");
        model.addAttribute("clase", service);
        model.addAttribute("listaCursos", courseRepository.findAll());
        model.addAttribute("listaEvaluaciones", EVALUATIONS);

        return "admin/ases-paq/formAsesPaq";
    }
    @GetMapping(value = "/send-email/{id}")
    public  String enviarCorreoClase(@PathVariable ("id") int id ,@RequestParam("mensaje") String mensaje) throws IOException, MessagingException {
        List<User> listaInscritos = new ArrayList<>() ;

        List<Enrollment> listaEnrollment = enrollmentRepository.findClaseEnrollByClase_Idclase(id);
        for (Enrollment c: listaEnrollment){
            User u=c.getStudent();
            listaInscritos.add(u);
        }
        customEmailService.sendMailToUsersMensaje(listaInscritos, listaEnrollment.get(0).getClase(),mensaje);

        return "redirect:/admin/ases-paq/lis-ins/"+ listaEnrollment.get(0).getClase().getIdService();
    }

    @GetMapping(value = "/edit/ases-paq/{id}")
    public String editarPaqueteAsesoria(Model model, Service service, HttpSession session, @PathVariable("id") int id ){

        model.addAttribute("universidad", UNIVERSITIES);

        model.addAttribute("asesores", userRepository.findAllByRol_IdrolAndActivoIsTrue(2));
        User user = (User) session.getAttribute("usuario");
       Optional<Service> optClase = serviceRepository.findByIdclaseAndDisponibleIsTrue(id);
       if (optClase.isPresent()){
           service =optClase.get();
        service.setCurso(new Course(user.getUniversity()));
        service.setExpiration(LocalDate.now().plusMonths(1));
        service.setClaseSesions(new ArrayList<>());
        service.setPrice(BigDecimal.valueOf(PRECIO_BASE_ASES_PAQ_PUCP));
        service.setTeacher(user);
        for (int i=0; i<5;i++) service.getClaseSesions().add(new EnrollmentSession(service));
        model.addAttribute("title","Nuevo Paquete de Asesorías");
        model.addAttribute("clase", service);
        model.addAttribute("listaCursos", courseRepository.findAllByCursoId_Universidad(user.getUniversity()));
        model.addAttribute("listaEvaluaciones", EVALUATIONS);

        return "admin/crearClase";
       }

       else{
           return "redirect:/admin/new/ases-paq";

       }
    }
    @PostMapping("ases-paq/save")
    public String saveAsesPaq(Service service, BindingResult bindingResult,
                              HttpSession session, Model model, RedirectAttributes attributes){
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        EnrollmentSession enrollmentSession;

        User user = (User)session.getAttribute("usuario");
        Optional<Course> optionalCurso = courseRepository.findById(service.getCurso().getCursoId());

        if (!EVALUATIONS.containsKey(service.getEvaluation()))
            bindingResult.rejectValue("evaluacion","error.user","Elija la evaluación");

        if (service.getSessionsNumber()<0 || service.getSessionsNumber()>5)
            bindingResult.rejectValue("numSesiones","error.user", "Elija el número de sesiones");

        if (!UNIVERSITIES.containsKey(service.getCurso().getCursoId().getUniversity()) ||
                !(user.getUniversity().equals(service.getCurso().getCursoId().getUniversity())))
            bindingResult.rejectValue("curso.cursoId.universidad","error.user", "Elija la universidad");

        if (!optionalCurso.isPresent())
            bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Elija el curso");

        if (service.getIdService()==0){//Si es nuevo

            Optional<Service> optionalClase = serviceRepository.findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndEvaluacionAndDisponibleIsTrue
                    (user.getIdUser(), SERVICIO_ASESORIA_PAQUETE, service.getCurso().getCursoId(), service.getEvaluation());

            if (optionalClase.isPresent())
                bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Ya existe una asesoría para este curso");


            // VALIDACION DE LAS SESIONES DE LA ASESORIA:
            if (!bindingResult.hasErrors()){
                for (int i = 0; i < service.getSessionsNumber(); i++){
                    enrollmentSession = service.getClaseSesions().get(i);

                    //Se valida la fecha
                    if (enrollmentSession.getDate()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].fecha","error.user", "Ingrese una fecha");
                    else if (LocalDateTime.now().minusHours(5).plusDays(1).toLocalDate().isAfter(enrollmentSession.getDate()) || LocalDate.now().plusMonths(2).isBefore(enrollmentSession.getDate()))
                        bindingResult.rejectValue("claseSesions["+i+"].fecha","error.user", "Ingrese una fecha válida");

                    //Se valida la hora de inicio y fin
                    if (enrollmentSession.getStart()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].inicio","error.user", "Ingrese un inicio");
                    else if (LocalTime.of(6,0).isAfter(enrollmentSession.getStart()) || LocalTime.of(23, 1).isBefore(enrollmentSession.getStart()))
                        bindingResult.rejectValue("claseSesions["+i+"].inicio","error.user", "Ingrese un hora de inicio válida: De 6:00 a 23:00");
                    else if (enrollmentSession.getEnd()!=null)
                    {
                        if (enrollmentSession.getEnd().isBefore(enrollmentSession.getStart()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "Ingrese una hora fin válida");

                        if (enrollmentSession.getStart().plusMinutes(30).isAfter(enrollmentSession.getEnd()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "La sesión no puede durar menos de 30 minutos");

                        if (enrollmentSession.getStart().isBefore(LocalTime.of(9,0)) && enrollmentSession.getStart().plusHours(3).isBefore(enrollmentSession.getEnd()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "La sesión no puede durar más de 3 horas");
                    }
                    if (enrollmentSession.getEnd()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "Ingrese un fin");
                }
            }

            if (bindingResult.hasErrors()){
                model.addAttribute("universidad", UNIVERSITIES);

                model.addAttribute("asesores", userRepository.findAllByRol_IdrolAndActivoIsTrue(2));

                service.getCurso().getCursoId().setUniversity(user.getUniversity());
                model.addAttribute("title","Nuevo Paquete de Asesoría");
                model.addAttribute("clase", service);
                model.addAttribute("listaCursos", courseRepository.findAllByCursoId_Universidad(user.getUniversity()));
                model.addAttribute("listaEvaluaciones", EVALUATIONS);
                return "admin/ases-paq/formAsesPaq";
            }
            //Si all está bien
            else {
                //TODO: schedule un job que desactive la asesoría luego de la última clase
                List<EnrollmentSession> enrollmentSessionList = new ArrayList<EnrollmentSession>(){{
                    for (int i = 0; i < service.getSessionsNumber(); i++)
                        add(new EnrollmentSession(service, service.getClaseSesions().get(i)));
                }};
                service.setClaseSesions(null);
                service.setServiceType(SERVICIO_ASESORIA_PAQUETE);
                service.setTeacher(user);
                service.setCurso(optionalCurso.orElse(null));

                service.setPhoto(service.getCurso().getPhoto());
                service.setAvailable(true);
                serviceRepository.save(service);
                enrollmentSessionList.forEach(claseSesion1 -> claseSesion1.setClase(service));
                enrollmentSessionRepository.saveAll(enrollmentSessionList);
                attributes.addFlashAttribute("msgSuccess","Paquete de asesorías creado correctamente");
                return "redirect:/admin/ases-paq";
            }
        }
        return "redirect:/admin/ases-paq";
    }
    //Activar y desactivar pago
    @GetMapping("/activarPago/{idclase}/{idusuario}")
    public String activarPago(@PathVariable("idclase") int idclase,@PathVariable("idusuario") int idusuario){
    Optional<Enrollment> optClaseEnroll= enrollmentRepository.findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(idclase,idusuario);
    if (optClaseEnroll.isPresent()){
    Enrollment enrollment =optClaseEnroll.get();
    enrollment.setPayed(true);
        }
        return "redirect:/admin/ases-paq/lis-ins/"+idclase;

    }
    @GetMapping("/desactivarPago/{idclase}/{idusuario}")
    public String desactivarPago(@PathVariable("idclase") int idclase,@PathVariable("idusuario") int idusuario){
        Optional<Enrollment> optClaseEnroll= enrollmentRepository.findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(idclase,idusuario);
        if (optClaseEnroll.isPresent()){
            Enrollment enrollment =optClaseEnroll.get();
            enrollment.setPayed(false);
        }
        return "redirect:/admin/ases-paq/lis-ins/"+idclase;

    }
    @PostMapping("/ases-paq/delete")
    public String deleteAsesPaq(@RequestParam("idclase") Integer id, HttpSession session, RedirectAttributes attributes) throws IOException, MessagingException {
        User user = (User)session.getAttribute("usuario");
        Optional<Service> claseOptional = serviceRepository.
                findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PAQUETE);
        if (claseOptional.isPresent()){
            Service service = claseOptional.get();

            List<Sale> ventasDeClase =  saleRepository.findVentasByClaseEnroll_Clase(service);

            if (!ventasDeClase.isEmpty()){
                //TODO: notificar a los usuarios via correo que la clase fue cancelada
                List<User> listaInscritos = new ArrayList<>() ;

                List<Enrollment> listaEnrollment = enrollmentRepository.findClaseEnrollByClase_Idclase(service.getIdService());
                for (Enrollment c: listaEnrollment){
                    User u=c.getStudent();
                    listaInscritos.add(u);
                }
                String mensaje="Lamentamos informarles que la clase ha sido cancelada por motivos que escapan de " +
                        "manos de la academia.Esperamos su comprensión. Nos pondremos en contacto a la brevedad para reponer el dinero en caso se haya realizado el pago." +
                        "Gracias por su comprensión, esperamos contar con su preferencia siempre.";
                customEmailService.sendMailToUsersMensaje(listaInscritos, listaEnrollment.get(0).getClase(),mensaje);
                saleCanceledRepository.saveAll(SaleCanceled.generateVentaCanceladas(ventasDeClase));
                saleRepository.deleteAll(ventasDeClase);
            }
            enrollmentSessionRepository.deleteAll(service.getClaseSesions());
            enrollmentRepository.deleteAll(service.getClaseEnrollList());
            serviceRepository.delete(service);
            attributes.addFlashAttribute("msgSuccess", "Paquete de Asesorías eliminado correctamente");

        }else {
            attributes.addFlashAttribute("msgError", "No se encontró la clase");
        }
        return "redirect:/admin/ases-paq/";
    }

*/
}
