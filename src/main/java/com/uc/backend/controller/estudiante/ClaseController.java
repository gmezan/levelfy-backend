package com.uc.backend.controller.estudiante;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/c")
public class ClaseController {

    /*
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentSessionRepository enrollmentSessionRepository;
    //----------------------------------------- Maratones ----------------------------------------------------------------
    @GetMapping(value = {"/maratones", "/maratones/"})
    public String maratonesAsesoria(Model model) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
List <Service>listaMaratones=new ArrayList<>();
       if(!lista.isEmpty()) {
           for (int i=0;i< lista.size();i++ ) {
               List<EnrollmentSession> listaClaseSession = enrollmentSessionRepository.findByClase_Idclase(lista.get(i).getIdService());
               if (listaClaseSession.size()==1) {
                   listaMaratones.add(lista.get(i));
               }

           }

           }
       model.addAttribute("listaPaqueteAsesorias", listaMaratones);  //
        model.addAttribute("universidad", UNIVERSITIES);
        return "cursos/listaMaratones";
    }
    @GetMapping(value = "/maratones/{u}")
    public String maratonesAsesoriaOnlineAsd(Model model, @PathVariable("u") String univ) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_ASESORIA_PAQUETE,univ);

        List <Service>listaMaratones=new ArrayList<>();
        if(!lista.isEmpty()) {
            for (int i=0;i< lista.size();i++ ) {
                List<EnrollmentSession> listaClaseSession = enrollmentSessionRepository.findByClase_Idclase(lista.get(i).getIdService());
                if (listaClaseSession.size()==1) {
                    listaMaratones.add(lista.get(i)); } }}
        model.addAttribute("listaPaqueteAsesorias", listaMaratones);  //
        model.addAttribute("univ", univ);
        return "cursos/listaMaratones";
    }
    //----------------------------------------- PAQUETE ASESORIA -----------------------------------------------------------------
    @GetMapping(value = {"/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model model) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        model.addAttribute("listaPaqueteAsesorias", lista);  //
        model.addAttribute("universidad", UNIVERSITIES);
        return "cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = "/ases-paq/{u}")
    public String paqueteAsesoriaOnlineAsd(Model model, @PathVariable("u") String univ) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_ASESORIA_PAQUETE,univ);
        model.addAttribute("listaPaqueteAsesorias", lista);  //
        model.addAttribute("univ", univ);
        return "cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping("/ases-paq/enroll/{id}")
    public String paqueteAsesoriaOnline_asesoria(Model model, @ModelAttribute("paqueteAsesoria") Enrollment enrollment, @PathVariable("id") int id,
                                                 HttpSession session) {
        Optional<Service> optionalPaqueteAsesoria =
                serviceRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PAQUETE);

        User user = (User)session.getAttribute("usuario");
        if (user!=null){
            Optional<Enrollment> optionalClaseEnroll = enrollmentRepository.
                    findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(id, user.getIdUser());
            if (optionalClaseEnroll.isPresent()){
                return "redirect:/service/ases-paq/"+optionalClaseEnroll.get().getIdEnrollment();
            }
        }

        if (optionalPaqueteAsesoria.isPresent() &&
                (enrollmentSessionRepository.findByClase_Idclase(optionalPaqueteAsesoria.get().getIdService()).size()>0)) {
            enrollment.setClase(optionalPaqueteAsesoria.get());
            model.addAttribute("paqueteAsesoria", enrollment);
            model.addAttribute("fechas", enrollmentSessionRepository.findByClase_IdclaseOrderByFechaAscInicioAsc(id));
            return "cursos/formPaqueteAsesoriaOnline";
        }
        return "redirect:/c/ases-paq";
    }




    //----------------------------------------- ASESORIA PERSONALIZADA -----------------------------------------------------------
    @GetMapping(value = {"/ases-per", "/ases-per/" })
    public String onlineCourse(Model model) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PERSONALIZADA);
        model.addAttribute("listaAsesorias", lista);  //
        return "cursos/listaAsesoriaOnline";
    }

    @GetMapping(value = "/ases-per/{u}")
    public String onlineCourseUniv(Model model, @PathVariable("u") String univ) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_ASESORIA_PERSONALIZADA, univ);
        model.addAttribute("listaAsesorias", lista);  //
        model.addAttribute("univ", univ);
        return "cursos/listaAsesoriaOnline";
    }

    @GetMapping("/ases-per/enroll/{id}")
    public String onlineCourseAsesoria(Model model, @ModelAttribute("asesoria") Enrollment enrollment, @PathVariable("id") int id) {
        Optional<Service> optionalClase = serviceRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PERSONALIZADA);
        //List<Paquete> listaPaquetes = paqueteRepository.findAll();

        if (optionalClase.isPresent()) {
            enrollment.setClase(optionalClase.get());

            model.addAttribute("asesoria", enrollment);
            model.addAttribute("paquete", PAQUETES.get(1));
            return "cursos/formAsesoria"; //Envio a formulario de registro a Asesoria
        } else {
            return "redirect:/c/ases-per";
        }
    }






    //----------------------------------------- APRENDE A TU RITMO ---------------------------------------------------------------
    @GetMapping(value = {"/self-p","/self-p/"})
    public String selfPacedCourses(Model model) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_SELF_PACED);
        model.addAttribute("listaClases", lista);
        return "cursos/listaClaseSelfPaced";
    }

    @GetMapping(value = "/self-p/{u}")
    public String selfPacedCoursesAs(Model model, @PathVariable("u") String univ) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_SELF_PACED, univ);
        model.addAttribute("listaClases", lista);
        model.addAttribute("univ", univ);
        return "cursos/listaClaseSelfPaced";
    }

    //@GetMapping("/enroll/{id}")


*/


}
