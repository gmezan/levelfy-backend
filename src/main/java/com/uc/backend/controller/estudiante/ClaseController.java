package com.uc.backend.controller.estudiante;

import com.uc.backend.entity.Clase;
import com.uc.backend.entity.ClaseEnroll;
import com.uc.backend.entity.ClaseSesion;
import com.uc.backend.entity.Usuario;
import com.uc.backend.repository.ClaseEnrollRepository;
import com.uc.backend.repository.ClaseRepository;
import com.uc.backend.repository.ClaseSesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.uc.backend.utils.CustomConstants.*;

@Controller
@RequestMapping("/c")
public class ClaseController {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;
    //----------------------------------------- Maratones ----------------------------------------------------------------
    @GetMapping(value = {"/maratones", "/maratones/"})
    public String maratonesAsesoria(Model model) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
List <Clase>listaMaratones=new ArrayList<>();
       if(!lista.isEmpty()) {
           for (int i=0;i< lista.size();i++ ) {
               List<ClaseSesion> listaClaseSession = claseSesionRepository.findByClase_Idclase(lista.get(i).getIdclase());
               if (listaClaseSession.size()==1) {
                   listaMaratones.add(lista.get(i));
               }

           }

           }
       model.addAttribute("listaPaqueteAsesorias", listaMaratones);  //
        model.addAttribute("universidad",UNIVERSIDAD);
        return "cursos/listaMaratones";
    }
    @GetMapping(value = "/maratones/{u}")
    public String maratonesAsesoriaOnlineAsd(Model model, @PathVariable("u") String univ) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_ASESORIA_PAQUETE,univ);

        List <Clase>listaMaratones=new ArrayList<>();
        if(!lista.isEmpty()) {
            for (int i=0;i< lista.size();i++ ) {
                List<ClaseSesion> listaClaseSession = claseSesionRepository.findByClase_Idclase(lista.get(i).getIdclase());
                if (listaClaseSession.size()==1) {
                    listaMaratones.add(lista.get(i)); } }}
        model.addAttribute("listaPaqueteAsesorias", listaMaratones);  //
        model.addAttribute("univ", univ);
        return "cursos/listaMaratones";
    }
    //----------------------------------------- PAQUETE ASESORIA -----------------------------------------------------------------
    @GetMapping(value = {"/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model model) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        model.addAttribute("listaPaqueteAsesorias", lista);  //
        model.addAttribute("universidad",UNIVERSIDAD);
        return "cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = "/ases-paq/{u}")
    public String paqueteAsesoriaOnlineAsd(Model model, @PathVariable("u") String univ) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_ASESORIA_PAQUETE,univ);
        model.addAttribute("listaPaqueteAsesorias", lista);  //
        model.addAttribute("univ", univ);
        return "cursos/listaPaqueteAsesoriaOnline";
    }

    @GetMapping("/ases-paq/enroll/{id}")
    public String paqueteAsesoriaOnline_asesoria(Model model, @ModelAttribute("paqueteAsesoria") ClaseEnroll claseEnroll, @PathVariable("id") int id,
                                                 HttpSession session) {
        Optional<Clase> optionalPaqueteAsesoria =
                claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PAQUETE);

        Usuario user = (Usuario)session.getAttribute("usuario");
        if (user!=null){
            Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                    findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(id, user.getIdusuario());
            if (optionalClaseEnroll.isPresent()){
                return "redirect:/service/ases-paq/"+optionalClaseEnroll.get().getIdClaseEnroll();
            }
        }

        if (optionalPaqueteAsesoria.isPresent() &&
                (claseSesionRepository.findByClase_Idclase(optionalPaqueteAsesoria.get().getIdclase()).size()>0)) {
            claseEnroll.setClase(optionalPaqueteAsesoria.get());
            model.addAttribute("paqueteAsesoria", claseEnroll);
            model.addAttribute("fechas", claseSesionRepository.findByClase_IdclaseOrderByFechaAscInicioAsc(id));
            return "cursos/formPaqueteAsesoriaOnline";
        }
        return "redirect:/c/ases-paq";
    }




    //----------------------------------------- ASESORIA PERSONALIZADA -----------------------------------------------------------
    @GetMapping(value = {"/ases-per", "/ases-per/" })
    public String onlineCourse(Model model) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PERSONALIZADA);
        model.addAttribute("listaAsesorias", lista);  //
        return "cursos/listaAsesoriaOnline";
    }

    @GetMapping(value = "/ases-per/{u}")
    public String onlineCourseUniv(Model model, @PathVariable("u") String univ) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_ASESORIA_PERSONALIZADA, univ);
        model.addAttribute("listaAsesorias", lista);  //
        model.addAttribute("univ", univ);
        return "cursos/listaAsesoriaOnline";
    }

    @GetMapping("/ases-per/enroll/{id}")
    public String onlineCourseAsesoria(Model model, @ModelAttribute("asesoria") ClaseEnroll claseEnroll, @PathVariable("id") int id) {
        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PERSONALIZADA);
        //List<Paquete> listaPaquetes = paqueteRepository.findAll();

        if (optionalClase.isPresent()) {
            claseEnroll.setClase(optionalClase.get());

            model.addAttribute("asesoria", claseEnroll);
            model.addAttribute("paquete", PAQUETES.get(1));
            return "cursos/formAsesoria"; //Envio a formulario de registro a Asesoria
        } else {
            return "redirect:/c/ases-per";
        }
    }






    //----------------------------------------- APRENDE A TU RITMO ---------------------------------------------------------------
    @GetMapping(value = {"/self-p","/self-p/"})
    public String selfPacedCourses(Model model) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_SELF_PACED);
        model.addAttribute("listaClases", lista);
        return "cursos/listaClaseSelfPaced";
    }

    @GetMapping(value = "/self-p/{u}")
    public String selfPacedCoursesAs(Model model, @PathVariable("u") String univ) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(SERVICIO_SELF_PACED, univ);
        model.addAttribute("listaClases", lista);
        model.addAttribute("univ", univ);
        return "cursos/listaClaseSelfPaced";
    }

    //@GetMapping("/enroll/{id}")



}
