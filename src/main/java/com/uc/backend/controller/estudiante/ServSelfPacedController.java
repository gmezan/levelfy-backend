package com.uc.backend.controller.estudiante;

import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.uc.backend.utils.CustomConstants.*;

@Controller
public class ServSelfPacedController {
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
    VideoRepository videoRepository;

    @Autowired
    VentaRepository ventaRepository;


    //clase de Aprende a tu ritmo
    @GetMapping("/selfPaced/clase")
    public String selfPacedClase(Model model, @RequestParam("id") int id, HttpSession session) {
        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_SELF_PACED);

        if (optionalClase.isPresent()) {
            Clase clase = optionalClase.get();
            if (session.getAttribute("usuario") != null) { // si el usuario ya está registrado e inscrito en la clase
                Usuario user = (Usuario) session.getAttribute("usuario");
                List<ClaseEnroll> claseEnrollList =
                        claseEnrollRepository
                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_IdclaseAndClase_DisponibleIsTrue(user.getIdusuario(), clase.getIdclase());
                if (!claseEnrollList.isEmpty()) {
                    return "redirect:/u/miClase?id=" + claseEnrollList.get(0).getIdClaseEnroll();
                }
            }

            List<Video> v_aux;
            List<List<Video>> lista = new ArrayList<>();
            for (Integer eval : EVALUACION.keySet()) {
                v_aux = videoRepository.findVideosByClase_IdclaseAndEvaluacionOrderByOrdenAsc(clase.getIdclase(), eval);
                if (!v_aux.isEmpty()) {
                    lista.add(v_aux);
                }
            }
            model.addAttribute("clase", clase); // Nombre Curso, profesor
            model.addAttribute("map", lista); //Orden de videos
            return "cursos/preFormSelfPaced"; //Envio a formulario de registro
        } else {
            return "redirect:/selfPaced";
        }
    }

    @GetMapping("/selfPaced/form")
    public String selfPacedForm(Model model, @RequestParam("id") int idclase) {
        Optional<Clase> optionalClase = claseRepository.findById(idclase);
        if (optionalClase.isPresent()) {
            Clase clase = optionalClase.get();
            model.addAttribute("clase", clase); // Nombre Curso, profesor
            model.addAttribute("tiempos", TIEMPOCLASE.keySet());
            return "cursos/formSelfPaced"; //Envio a formulario de registro
        } else {
            return "redirect:/selfPaced";
        }

    }

    @PostMapping("/curso/processSelfPaced")
    public String cursoProcessSelfPaced(Model model,
                                        @RequestParam(name = "idclase") int idclase,
                                        @RequestParam(name = "tiempo") int idtiempo,
                                        HttpSession session,
                                        RedirectAttributes attributes) {

        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndDisponibleIsTrue(idclase);

        //Tiempo clase es cuanto tiempo estará disponible el curso: 1mes, 2 meses, 6 meses etc
        if (optionalClase.isPresent() && TIEMPOCLASE.containsKey(idtiempo)) {
            ClaseEnroll claseEnroll = new ClaseEnroll();
            Venta pagoClase = new Venta();
            Clase clase = optionalClase.get();
            claseEnroll.setClase(clase);
            pagoClase.setMonto(claseEnroll.getClase().getPrecio());
            claseEnroll.setClase(clase);
            claseEnroll.setEstudiante((Usuario) session.getAttribute("usuario"));
            claseEnroll.setPagado(false); // está activo
            claseEnrollRepository.save(claseEnroll); // Asesoria registrada
            pagoClase.setClaseEnroll(claseEnroll);
            ventaRepository.save(pagoClase);
            return "redirect:/u/selfPaced/confirmClase?id=" + claseEnroll.getIdClaseEnroll(); //Envio a confirmacion de registro
        } else {
            return "redirect:/selfPaced";
        }
    }

}
