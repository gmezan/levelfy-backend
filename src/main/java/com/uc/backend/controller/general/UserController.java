package com.uc.backend.controller.general;

import com.uc.backend.entity.*;
import com.uc.backend.enums.RoleName;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.*;
import com.uc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("model/user")
public class UserController {
    UserRepository userRepository;
    UserService userService;
    RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }


    @GetMapping(value = "me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getCurrentUser() {

        return userService.getCurrentUser()
                .map(user -> new ResponseEntity<>(user, OK))
                .orElseGet(() -> new ResponseEntity<>(null, BAD_REQUEST));

    }


    // RESTFUL

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(name = "u", required = false) UniversityName u,
            @RequestParam(name = "r", required = false) RoleName r
    ) {
        Role role =  null;
        if (r!=null) role = roleRepository.findByName(r).orElse(null);

        if (u!=null && role!=null){
            Role finalRole = role;
            return new ResponseEntity<>(userRepository.findUsersByUniversity(u).stream()
                    .filter(user -> user.getRole().contains(finalRole)).collect(Collectors.toList()), OK);
        }
        else if (u!=null){
            return new ResponseEntity<>(userRepository.findUsersByUniversity(u), OK);
        }
        else if (role!=null){
            Role finalRole = role;
            return new ResponseEntity<>(userRepository.findAll().stream()
                    .filter(user -> user.getRole().contains(finalRole)).collect(Collectors.toList()), OK);
        } else return new ResponseEntity<>(userRepository.findAll(), OK);
    }

    @GetMapping(value = "{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("u") int userId) {
        return userRepository.findById(userId)
                .map(value -> new ResponseEntity<>(value, OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> newUser(@RequestBody User User) {
        return userRepository.findById(User.getIdUser())
                .map(value -> new ResponseEntity<>(value, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> new ResponseEntity<>(userRepository.save(User), OK));
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User User) {
        return userRepository.findById(User.getIdUser())
                .map(value -> new ResponseEntity<>(userRepository.save(User), OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUser(@PathVariable("u") int userId) {
        return userRepository.findById(userId)
                .map(value -> {
                    userRepository.delete(value);
                    return new ResponseEntity(OK);
                })
                .orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));
    }
    

    
    
    
/*
    @GetMapping(value = {"","/"})
    public String uMisCursos(Model entity, HttpSession session){
        User user = (User) session.getAttribute("usuario");
        entity.addAttribute("listaAsesoriaOnline", enrollmentRepository
                                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(user.getIdUser(),SERVICIO_ASESORIA_PERSONALIZADA));
        entity.addAttribute("listaClaseSelfPaced", enrollmentRepository
                                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(user.getIdUser(),SERVICIO_SELF_PACED));
        entity.addAttribute("listaPaquetesAsesoria", enrollmentRepository
                                                .findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(user.getIdUser(),SERVICIO_ASESORIA_PAQUETE));
        return "client/misCursos";
    }


    /*
    @GetMapping("/miClase")
    public String uMiClase(Model entity, HttpSession session, ServletRequest request){
        Map<String, String[]> paramMap = request.getParameterMap();
        Usuario user = (Usuario) session.getAttribute("usuario");
        int idClaseEnroll = Integer.parseInt(paramMap.get("id")[0]);
        System.out.println(idClaseEnroll);
        Optional<ClaseEnroll> optionalClaseEnroll = enrollmentRepository.findById(idClaseEnroll);
        List<ClaseEnroll> claseEnrolls = enrollmentRepository.findClaseEnrollsByEstudiante_IdusuarioAndActivoIsTrue(user.getIdusuario());
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
                entity.addAttribute("claseEnroll", claseEnroll);
                if(session.getAttribute(Integer.toString(idClaseEnroll)) == null ){
                    List<Evaluacion> evaluaciones = evaluacionRepository.findByOrderByIdevaluacionAsc();
                    List<Video> v_aux ;
                    for(Evaluacion eval :  evaluaciones){
                        v_aux = videoRepository.findVideosByClase_IdclaseAndEvaluacion_IdevaluacionOrderByOrdenAsc(claseEnroll.getClase().getIdclase(), eval.getIdevaluacion());
                        if(!v_aux.isEmpty()){ lista.add(v_aux); }
                    }
                    session.setAttribute(Integer.toString(idClaseEnroll), lista);
                    entity.addAttribute("clase", claseEnroll.getClase()); // Nombre Curso, profesor
                    entity.addAttribute("map", lista); //Orden de videos
                }
                else{
                    entity.addAttribute("clase", claseEnroll.getClase()); // Nombre Curso, profesor
                    entity.addAttribute("map", session.getAttribute(Integer.toString(idClaseEnroll))); //Orden de videos
                }

                if(paramMap.containsKey("video")){
                    int idvideo = Integer.parseInt(paramMap.get("video")[0]);
                    Optional<Video> optionalVideo = videoRepository.findById(idvideo);
                    if(optionalVideo.isPresent()){
                        Video vd = optionalVideo.get();
                        if(!claseEnroll.getClase().equals(vd.getClase())){
                            return "redirect:/u/misCursos";
                        }
                        entity.addAttribute("videosrc", vd);
                    }
                }

                return "client/claseSelfPaced";

            }
        }
        return "redirect:/u/misCursos";

    }

    @GetMapping("/selfPaced/confirmClase")
    public String confirmClase(Model entity, @RequestParam(name = "id") int idClaseEnroll,
                                  HttpSession session)
    {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll =
                enrollmentRepository.findById(idClaseEnroll);

        if(optionalClaseEnroll.isPresent()){
            ClaseEnroll claseEnroll= optionalClaseEnroll.get();
            if(claseEnroll.getEstudiante().getIdusuario() == user.getIdusuario()){
                entity.addAttribute("claseEnroll", claseEnroll);
                return "client/confirmSelfPaced";
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
                enrollmentRepository.findById(idClaseEnroll);
        if (optionalClaseEnroll.isPresent()){
            ClaseEnroll claseEnroll = optionalClaseEnroll.get();
            if(claseEnroll.getEstudiante().getIdusuario() == user.getIdusuario()){
                PagoClase pagoClase = claseEnroll.getPagoClase();
                claseEnroll.setPagoClase(null);
                pagoClase.setClaseEnroll(null);
                enrollmentRepository.save(claseEnroll);
                pagoClaseRepository.save(pagoClase);
                enrollmentRepository.deleteById(claseEnroll.getIdClaseEnroll());
                pagoClaseRepository.deleteById(pagoClase.getIdpago_clase());

                attributes.addFlashAttribute("msgCorrect", "Asesoría cancelada correctamente");
            }
        }
        return "redirect:/u/misCursos";
    }
*/
}
