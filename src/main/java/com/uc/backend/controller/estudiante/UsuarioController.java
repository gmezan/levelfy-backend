package com.uc.backend.controller.estudiante;

import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static com.uc.backend.utils.CustomConstants.*;


@Controller
@RequestMapping("/u")
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;


    @GetMapping(value = {"","/"})
    public String uMisCursos(Model model, HttpSession session){
        Usuario user = (Usuario) session.getAttribute("usuario");
        model.addAttribute("listaAsesoriaOnline", claseEnrollRepository
                                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA));
        model.addAttribute("listaClaseSelfPaced", claseEnrollRepository
                                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(user.getIdusuario(),SERVICIO_SELF_PACED));
        model.addAttribute("listaPaquetesAsesoria",claseEnrollRepository
                                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(user.getIdusuario(),SERVICIO_ASESORIA_PAQUETE));
        return "estudiante/misCursos";
    }


    /*
    @GetMapping("/miClase")
    public String uMiClase(Model model, HttpSession session, ServletRequest request){
        Map<String, String[]> paramMap = request.getParameterMap();
        Usuario user = (Usuario) session.getAttribute("usuario");
        int idClaseEnroll = Integer.parseInt(paramMap.get("id")[0]);
        System.out.println(idClaseEnroll);
        Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.findById(idClaseEnroll);
        List<ClaseEnroll> claseEnrolls = claseEnrollRepository.findClaseEnrollsByEstudiante_IdusuarioAndActivoIsTrue(user.getIdusuario());
        List<List<Video>> lista = new ArrayList<>();

        //redirec if is not payed
        if(optionalClaseEnroll.isPresent()){
            ClaseEnroll claseEnroll = optionalClaseEnroll.get();
            if (claseEnrolls.contains(claseEnroll) && claseEnroll.isActivo()){ //  está inscrito
                if(!claseEnroll.getPagoClase().isPago()){
                    //Si es que NO pago
                    return "redirect:/u/selfPaced/confirmClase?id=" + claseEnroll.getIdClaseEnroll();
                }

                //Si pago, Enviar a plataforma de video
                model.addAttribute("claseEnroll", claseEnroll);
                if(session.getAttribute(Integer.toString(idClaseEnroll)) == null ){
                    List<Evaluacion> evaluaciones = evaluacionRepository.findByOrderByIdevaluacionAsc();
                    List<Video> v_aux ;
                    for(Evaluacion eval :  evaluaciones){
                        v_aux = videoRepository.findVideosByClase_IdclaseAndEvaluacion_IdevaluacionOrderByOrdenAsc(claseEnroll.getClase().getIdclase(), eval.getIdevaluacion());
                        if(!v_aux.isEmpty()){ lista.add(v_aux); }
                    }
                    session.setAttribute(Integer.toString(idClaseEnroll), lista);
                    model.addAttribute("clase", claseEnroll.getClase()); // Nombre Curso, profesor
                    model.addAttribute("map", lista); //Orden de videos
                }
                else{
                    model.addAttribute("clase", claseEnroll.getClase()); // Nombre Curso, profesor
                    model.addAttribute("map", session.getAttribute(Integer.toString(idClaseEnroll))); //Orden de videos
                }

                if(paramMap.containsKey("video")){
                    int idvideo = Integer.parseInt(paramMap.get("video")[0]);
                    Optional<Video> optionalVideo = videoRepository.findById(idvideo);
                    if(optionalVideo.isPresent()){
                        Video vd = optionalVideo.get();
                        if(!claseEnroll.getClase().equals(vd.getClase())){
                            return "redirect:/u/misCursos";
                        }
                        model.addAttribute("videosrc", vd);
                    }
                }

                return "estudiante/claseSelfPaced";

            }
        }
        return "redirect:/u/misCursos";

    }

    @GetMapping("/selfPaced/confirmClase")
    public String confirmClase(Model model, @RequestParam(name = "id") int idClaseEnroll,
                                  HttpSession session)
    {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll =
                claseEnrollRepository.findById(idClaseEnroll);

        if(optionalClaseEnroll.isPresent()){
            ClaseEnroll claseEnroll= optionalClaseEnroll.get();
            if(claseEnroll.getEstudiante().getIdusuario() == user.getIdusuario()){
                model.addAttribute("claseEnroll", claseEnroll);
                return "estudiante/confirmSelfPaced";
            }
        }
        return "redirect:/u/misCursos";

    }


    @GetMapping("/cancelarClase")
    public String cancelarClase(HttpSession session,
                                   @RequestParam(name = "id", required = true) int idClaseEnroll,
                                   RedirectAttributes attributes){

        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll =
                claseEnrollRepository.findById(idClaseEnroll);
        if (optionalClaseEnroll.isPresent()){
            ClaseEnroll claseEnroll = optionalClaseEnroll.get();
            if(claseEnroll.getEstudiante().getIdusuario() == user.getIdusuario()){
                PagoClase pagoClase = claseEnroll.getPagoClase();
                claseEnroll.setPagoClase(null);
                pagoClase.setClaseEnroll(null);
                claseEnrollRepository.save(claseEnroll);
                pagoClaseRepository.save(pagoClase);
                claseEnrollRepository.deleteById(claseEnroll.getIdClaseEnroll());
                pagoClaseRepository.deleteById(pagoClase.getIdpago_clase());

                attributes.addFlashAttribute("msgCorrect", "Asesoría cancelada correctamente");
            }
        }
        return "redirect:/u/misCursos";
    }
*/
}
