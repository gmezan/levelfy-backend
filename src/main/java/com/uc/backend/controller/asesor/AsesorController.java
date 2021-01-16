package com.uc.backend.controller.asesor;

import com.uc.backend.entity.Service;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.User;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.EnrollmentSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.uc.backend.utils.CustomConstants.*;

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


    //Se muestran los cursos (disponibles) que está dictando el asesor
    @GetMapping(value = {"","/"})
    public String uMisCursos(Model model, HttpSession session){
        User user = (User) session.getAttribute("usuario");
        model.addAttribute("listaAsesoriaOnline", serviceRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdUser(),SERVICIO_ASESORIA_PERSONALIZADA));
        model.addAttribute("listaClaseSelfPaced", serviceRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdUser(),SERVICIO_SELF_PACED));
        model.addAttribute("listaPaquetesAsesoria", serviceRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdUser(),SERVICIO_ASESORIA_PAQUETE));

//Validar si tiene los datos necesarios para el funcionamiento de la web
        User usuario = (User) session.getAttribute("usuario");
        if (usuario!=null){
            try {
                if ((usuario.getPhone()==0)  || (usuario.getUniversity().isEmpty()) ){
                    System.out.println("I'm here");
                    model.addAttribute("isDataComplete",false);
                    model.addAttribute("universidad", UNIVERSITIES);

                }else{
                    System.out.println("data complete");
                    model.addAttribute("isDataComplete",true);

                }
            } catch ( Exception e) {
                System.out.println("I'm here 2");

                model.addAttribute("isDataComplete",false);
                model.addAttribute("universidad", UNIVERSITIES);
            }

        }else{
            model.addAttribute("isDataComplete",true);

        }
        return "asesor/dictando";
    }


    @GetMapping(value = {"/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model model) {
        List<Service> lista = serviceRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        model.addAttribute("listaPaqueteAsesorias", lista);  //
        return "/asesor/cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = {"/ases-paq/lis-ins/{id}", "/ases-paq/"})
    public String mostrarListaDeInscritos(Model model, @PathVariable("id") int id) {
        List<User> listaInscritos = new ArrayList<>() ;

        List<Enrollment> lista = enrollmentRepository.findClaseEnrollByClase_Idclase(id);
        for (Enrollment c: lista){
             User u=c.getStudent();
        listaInscritos.add(u);
        }
        model.addAttribute("listaInscritos", listaInscritos);
        return "/asesor/listaInscritos";}


        @GetMapping(value = "/new/ases-paq")
        public String crearPaqueteAsesoria(Model model, Service service){

        model.addAttribute("universidad", UNIVERSITIES);



        return "crearClaseAdmin";
    }
*/
}
