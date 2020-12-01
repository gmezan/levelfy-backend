package com.uc.backend.controller.asesor;

import com.uc.backend.entity.Clase;
import com.uc.backend.entity.ClaseEnroll;
import com.uc.backend.entity.Usuario;
import com.uc.backend.repository.ClaseEnrollRepository;
import com.uc.backend.repository.ClaseRepository;
import com.uc.backend.repository.ClaseSesionRepository;
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
    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;


    //Se muestran los cursos (disponibles) que está dictando el asesor
    @GetMapping(value = {"","/"})
    public String uMisCursos(Model model, HttpSession session){
        Usuario user = (Usuario) session.getAttribute("usuario");
        model.addAttribute("listaAsesoriaOnline", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA));
        model.addAttribute("listaClaseSelfPaced", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdusuario(),SERVICIO_SELF_PACED));
        model.addAttribute("listaPaquetesAsesoria",claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE));

//Validar si tiene los datos necesarios para el funcionamiento de la web
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario!=null){
            try {
                if ((usuario.getCelular()==0)  || (usuario.getUniversidad().isEmpty()) ){
                    System.out.println("I'm here");
                    model.addAttribute("isDataComplete",false);
                    model.addAttribute("universidad",UNIVERSIDAD);

                }else{
                    System.out.println("data complete");
                    model.addAttribute("isDataComplete",true);

                }
            } catch ( Exception e) {
                System.out.println("I'm here 2");

                model.addAttribute("isDataComplete",false);
                model.addAttribute("universidad",UNIVERSIDAD);
            }

        }else{
            model.addAttribute("isDataComplete",true);

        }
        return "asesor/dictando";
    }


    @GetMapping(value = {"/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model model) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        model.addAttribute("listaPaqueteAsesorias", lista);  //
        return "/asesor/cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = {"/ases-paq/lis-ins/{id}", "/ases-paq/"})
    public String mostrarListaDeInscritos(Model model, @PathVariable("id") int id) {
        List<Usuario> listaInscritos = new ArrayList<>() ;

        List<ClaseEnroll> lista = claseEnrollRepository.findClaseEnrollByClase_Idclase(id);
        for (ClaseEnroll c: lista){
             Usuario u=c.getEstudiante();
        listaInscritos.add(u);
        }
        model.addAttribute("listaInscritos", listaInscritos);
        return "/asesor/listaInscritos";}


        @GetMapping(value = "/new/ases-paq")
        public String crearPaqueteAsesoria(Model model, Clase clase){

        model.addAttribute("universidad",UNIVERSIDAD);



        return "crearClaseAdmin";
    }

}
