package com.uc.backend.controller.teach;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/a")
public class AsesorController {
    /*
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentSessionRepository enrollmentSessionRepository;


    //Se muestran los cursos (disponibles) que está dictando el teach
    @GetMapping(value = {"","/"})
    public String uMisCursos(Model entity, HttpSession session){
        User user = (User) session.getAttribute("usuario");
        entity.addAttribute("listaAsesoriaOnline", serviceRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdUser(),SERVICIO_ASESORIA_PERSONALIZADA));
        entity.addAttribute("listaClaseSelfPaced", serviceRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdUser(),SERVICIO_SELF_PACED));
        entity.addAttribute("listaPaquetesAsesoria", serviceRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdUser(),SERVICIO_ASESORIA_PAQUETE));

//Validar si tiene los datos necesarios para el funcionamiento de la web
        User usuario = (User) session.getAttribute("usuario");
        if (usuario!=null){
            try {
                if ((usuario.getPhone()==0)  || (usuario.getUniversity().isEmpty()) ){
                    System.out.println("I'm here");
                    entity.addAttribute("isDataComplete",false);
                    entity.addAttribute("universidad", UNIVERSITIES);

                }else{
                    System.out.println("data complete");
                    entity.addAttribute("isDataComplete",true);

                }
            } catch ( Exception e) {
                System.out.println("I'm here 2");

                entity.addAttribute("isDataComplete",false);
                entity.addAttribute("universidad", UNIVERSITIES);
            }

        }else{
            entity.addAttribute("isDataComplete",true);

        }
        return "teach/dictando";
    }


    @GetMapping(value = {"/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model entity) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        entity.addAttribute("listaPaqueteAsesorias", lista);  //
        return "/teach/cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = {"/ases-paq/lis-ins/{id}", "/ases-paq/"})
    public String mostrarListaDeInscritos(Model entity, @PathVariable("id") int id) {
        List<User> listaInscritos = new ArrayList<>() ;

        List<Enrollment> lista = enrollmentRepository.findClaseEnrollByClase_Idclase(id);
        for (Enrollment c: lista){
             User u=c.getStudent();
        listaInscritos.add(u);
        }
        entity.addAttribute("listaInscritos", listaInscritos);
        return "/teach/listaInscritos";}


        @GetMapping(value = "/new/ases-paq")
        public String crearPaqueteAsesoria(Model entity, Service service){

        entity.addAttribute("universidad", UNIVERSITIES);



        return "crearClaseAdmin";
    }
*/
}
