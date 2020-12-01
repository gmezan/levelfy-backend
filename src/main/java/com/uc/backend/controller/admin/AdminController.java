package com.uc.backend.controller.admin;

import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import com.uc.backend.service.CustomEmailService;
import dto.CantidadSugerencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    SugerenciaCursoRepository sugerenciaCursoRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    CursoRepository cursoRepository;
    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    CustomEmailService customEmailService;
    @Autowired
    VentaCanceladaRepository ventaCanceladaRepository;


    @GetMapping("/editarCurso")
    public String editarCurso(Model model){
        List<CantidadSugerencia> listaSugerencias = sugerenciaCursoRepository.cantidadSugerencias();
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PERSONALIZADA);
        model.addAttribute("listaSugerenciaCurso",listaSugerencias);
        model.addAttribute("listaAsesorias", lista);


        return "admin/editarCurso";
    }


    @GetMapping("/editarCurso/enroll/{id}")
    public String onlineCourseAsesoria(Model model, @ModelAttribute("asesoria") Clase clase, @PathVariable("id") int id, HttpSession session) {
        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PERSONALIZADA);
        //List<Paquete> listaPaquetes = paqueteRepository.findAll();
        Map<LocalDateTime, String> disponibilidades;

        if (optionalClase.isPresent()) {

            clase = optionalClase.get();
            model.addAttribute("asesoria", clase);
            model.addAttribute("paquete", PAQUETES.get(1));
            return "cursos/formCursoAdmin"; //Envio a formulario de registro a Asesoria
        } else {
            return "redirect:/admin/editarCurso";
        } }

//asesor

    //Se muestran los cursos (disponibles) que está dictando el asesor


    @GetMapping(value = {"","/","/ases-paq", "/ases-paq/"})
    public String paqueteAsesoriaOnline(Model model) {
        List<Clase> lista = claseRepository.findClasesByServicioAndDisponibleIsTrue(SERVICIO_ASESORIA_PAQUETE);
        model.addAttribute("listaPaqueteAsesorias", lista);  //

        return "admin/curso/listaPaqueteAsesoriaOnline";
    }

    @GetMapping(value = {"/ases-paq/lis-ins/{id}"})
    public String mostrarListaDeInscritos(Model model, @PathVariable("id") int id) {
        List<Usuario> listaInscritos = new ArrayList<>() ;

        List<ClaseEnroll> listaClaseEnroll = claseEnrollRepository.findClaseEnrollByClase_Idclase(id);
        for (ClaseEnroll c: listaClaseEnroll){
            Usuario u=c.getEstudiante();
            listaInscritos.add(u);
        }
        model.addAttribute("idclase",id);
        model.addAttribute("listaInscritos", listaClaseEnroll);
        return "admin/listaInscritosAdmin";
    }


    @GetMapping(value = "/new/ases-paq")
    public String crearPaqueteAsesoria(Model model, Clase clase, HttpSession session){



        model.addAttribute("asesores", usuarioRepository.findAllByRol_IdrolAndActivoIsTrue(2));
        List<String> universidad=cursoRepository.findUniversidades();
        model.addAttribute("universidad",universidad);

        Usuario user = (Usuario) session.getAttribute("usuario");
        clase = new Clase();

        clase.setVencimiento(LocalDate.now().plusMonths(1));
        clase.setClaseSesions(new ArrayList<>());
        clase.setPrecio(BigDecimal.valueOf(PRECIO_BASE_ASES_PAQ_PUCP));
        clase.setProfesor(user);
        for (int i=0; i<5;i++) clase.getClaseSesions().add(new ClaseSesion(clase));
        model.addAttribute("title","Nuevo Paquete de Asesorías");
        model.addAttribute("clase",clase);
        model.addAttribute("listaCursos",cursoRepository.findAll());
        model.addAttribute("listaEvaluaciones", EVALUACION);

        return "admin/ases-paq/formAsesPaq";
    }
    @GetMapping(value = "/send-email/{id}")
    public  String enviarCorreoClase(@PathVariable ("id") int id ,@RequestParam("mensaje") String mensaje) throws IOException, MessagingException {
        List<Usuario> listaInscritos = new ArrayList<>() ;

        List<ClaseEnroll> listaClaseEnroll = claseEnrollRepository.findClaseEnrollByClase_Idclase(id);
        for (ClaseEnroll c: listaClaseEnroll){
            Usuario u=c.getEstudiante();
            listaInscritos.add(u);
        }
        customEmailService.sendMailToUsersMensaje(listaInscritos,listaClaseEnroll.get(0).getClase(),mensaje);

        return "redirect:/admin/ases-paq/lis-ins/"+ listaClaseEnroll.get(0).getClase().getIdclase();
    }

    @GetMapping(value = "/edit/ases-paq/{id}")
    public String editarPaqueteAsesoria(Model model, Clase clase, HttpSession session,@PathVariable("id") int id ){

        model.addAttribute("universidad",UNIVERSIDAD);

        model.addAttribute("asesores", usuarioRepository.findAllByRol_IdrolAndActivoIsTrue(2));
        Usuario user = (Usuario) session.getAttribute("usuario");
       Optional<Clase> optClase = claseRepository.findByIdclaseAndDisponibleIsTrue(id);
       if (optClase.isPresent()){
           clase=optClase.get();
        clase.setCurso(new Curso(user.getUniversidad()));
        clase.setVencimiento(LocalDate.now().plusMonths(1));
        clase.setClaseSesions(new ArrayList<>());
        clase.setPrecio(BigDecimal.valueOf(PRECIO_BASE_ASES_PAQ_PUCP));
        clase.setProfesor(user);
        for (int i=0; i<5;i++) clase.getClaseSesions().add(new ClaseSesion(clase));
        model.addAttribute("title","Nuevo Paquete de Asesorías");
        model.addAttribute("clase",clase);
        model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
        model.addAttribute("listaEvaluaciones", EVALUACION);

        return "admin/crearClase";
       }

       else{
           return "redirect:/admin/new/ases-paq";

       }
    }
    @PostMapping("ases-paq/save")
    public String saveAsesPaq(@ModelAttribute("clase") @Valid Clase clase, BindingResult bindingResult,
                              HttpSession session, Model model, RedirectAttributes attributes){
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ClaseSesion claseSesion;

        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Curso> optionalCurso = cursoRepository.findById(clase.getCurso().getCursoId());

        if (!EVALUACION.containsKey(clase.getEvaluacion()))
            bindingResult.rejectValue("evaluacion","error.user","Elija la evaluación");

        if (clase.getNumSesiones()<0 || clase.getNumSesiones()>5)
            bindingResult.rejectValue("numSesiones","error.user", "Elija el número de sesiones");

        if (!UNIVERSIDAD.containsKey(clase.getCurso().getCursoId().getUniversidad()) ||
                !(user.getUniversidad().equals(clase.getCurso().getCursoId().getUniversidad())))
            bindingResult.rejectValue("curso.cursoId.universidad","error.user", "Elija la universidad");

        if (!optionalCurso.isPresent())
            bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Elija el curso");

        if (clase.getIdclase()==0){//Si es nuevo

            Optional<Clase> optionalClase = claseRepository.findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndEvaluacionAndDisponibleIsTrue
                    (user.getIdusuario(), SERVICIO_ASESORIA_PAQUETE, clase.getCurso().getCursoId(), clase.getEvaluacion());

            if (optionalClase.isPresent())
                bindingResult.rejectValue("curso.cursoId.idcurso","error.user", "Ya existe una asesoría para este curso");


            // VALIDACION DE LAS SESIONES DE LA ASESORIA:
            if (!bindingResult.hasErrors()){
                for (int i = 0; i < clase.getNumSesiones(); i++){
                    claseSesion = clase.getClaseSesions().get(i);

                    //Se valida la fecha
                    if (claseSesion.getFecha()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].fecha","error.user", "Ingrese una fecha");
                    else if (LocalDateTime.now().minusHours(5).plusDays(1).toLocalDate().isAfter(claseSesion.getFecha()) || LocalDate.now().plusMonths(2).isBefore(claseSesion.getFecha()))
                        bindingResult.rejectValue("claseSesions["+i+"].fecha","error.user", "Ingrese una fecha válida");

                    //Se valida la hora de inicio y fin
                    if (claseSesion.getInicio()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].inicio","error.user", "Ingrese un inicio");
                    else if (LocalTime.of(6,0).isAfter(claseSesion.getInicio()) || LocalTime.of(23, 1).isBefore(claseSesion.getInicio()))
                        bindingResult.rejectValue("claseSesions["+i+"].inicio","error.user", "Ingrese un hora de inicio válida: De 6:00 a 23:00");
                    else if (claseSesion.getFin()!=null)
                    {
                        if (claseSesion.getFin().isBefore(claseSesion.getInicio()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "Ingrese una hora fin válida");

                        if (claseSesion.getInicio().plusMinutes(30).isAfter(claseSesion.getFin()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "La sesión no puede durar menos de 30 minutos");

                        if (claseSesion.getInicio().isBefore(LocalTime.of(9,0)) && claseSesion.getInicio().plusHours(3).isBefore(claseSesion.getFin()))
                            bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "La sesión no puede durar más de 3 horas");
                    }
                    if (claseSesion.getFin()==null)
                        bindingResult.rejectValue("claseSesions["+i+"].fin","error.user", "Ingrese un fin");
                }
            }

            if (bindingResult.hasErrors()){
                model.addAttribute("universidad",UNIVERSIDAD);

                model.addAttribute("asesores", usuarioRepository.findAllByRol_IdrolAndActivoIsTrue(2));

                clase.getCurso().getCursoId().setUniversidad(user.getUniversidad());
                model.addAttribute("title","Nuevo Paquete de Asesoría");
                model.addAttribute("clase",clase);
                model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
                model.addAttribute("listaEvaluaciones", EVALUACION);
                return "admin/ases-paq/formAsesPaq";
            }
            //Si all está bien
            else {
                //TODO: schedule un job que desactive la asesoría luego de la última clase
                List<ClaseSesion> claseSesionList = new ArrayList<ClaseSesion>(){{
                    for (int i = 0; i < clase.getNumSesiones(); i++)
                        add(new ClaseSesion(clase, clase.getClaseSesions().get(i)));
                }};
                clase.setClaseSesions(null);
                clase.setServicio(SERVICIO_ASESORIA_PAQUETE);
                clase.setProfesor(user);
                clase.setCurso(optionalCurso.orElse(null));

                clase.setFoto(clase.getCurso().getFoto());
                clase.setDisponible(true);
                claseRepository.save(clase);
                claseSesionList.forEach(claseSesion1 -> claseSesion1.setClase(clase));
                claseSesionRepository.saveAll(claseSesionList);
                attributes.addFlashAttribute("msgSuccess","Paquete de asesorías creado correctamente");
                return "redirect:/admin/ases-paq";
            }
        }
        return "redirect:/admin/ases-paq";
    }
    //Activar y desactivar pago
    @GetMapping("/activarPago/{idclase}/{idusuario}")
    public String activarPago(@PathVariable("idclase") int idclase,@PathVariable("idusuario") int idusuario){
    Optional<ClaseEnroll> optClaseEnroll=claseEnrollRepository.findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(idclase,idusuario);
    if (optClaseEnroll.isPresent()){
    ClaseEnroll claseEnroll =optClaseEnroll.get();
    claseEnroll.setPagado(true);
        }
        return "redirect:/admin/ases-paq/lis-ins/"+idclase;

    }
    @GetMapping("/desactivarPago/{idclase}/{idusuario}")
    public String desactivarPago(@PathVariable("idclase") int idclase,@PathVariable("idusuario") int idusuario){
        Optional<ClaseEnroll> optClaseEnroll=claseEnrollRepository.findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(idclase,idusuario);
        if (optClaseEnroll.isPresent()){
            ClaseEnroll claseEnroll =optClaseEnroll.get();
            claseEnroll.setPagado(false);
        }
        return "redirect:/admin/ases-paq/lis-ins/"+idclase;

    }
    @PostMapping("/ases-paq/delete")
    public String deleteAsesPaq(@RequestParam("idclase") Integer id, HttpSession session, RedirectAttributes attributes) throws IOException, MessagingException {
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<Clase> claseOptional = claseRepository.
                findByIdclaseAndServicioAndDisponibleIsTrue(id,SERVICIO_ASESORIA_PAQUETE);
        if (claseOptional.isPresent()){
            Clase clase = claseOptional.get();

            List<Venta> ventasDeClase =  ventaRepository.findVentasByClaseEnroll_Clase(clase);

            if (!ventasDeClase.isEmpty()){
                //TODO: notificar a los usuarios via correo que la clase fue cancelada
                List<Usuario> listaInscritos = new ArrayList<>() ;

                List<ClaseEnroll> listaClaseEnroll = claseEnrollRepository.findClaseEnrollByClase_Idclase(clase.getIdclase());
                for (ClaseEnroll c: listaClaseEnroll){
                    Usuario u=c.getEstudiante();
                    listaInscritos.add(u);
                }
                String mensaje="Lamentamos informarles que la clase ha sido cancelada por motivos que escapan de " +
                        "manos de la academia.Esperamos su comprensión. Nos pondremos en contacto a la brevedad para reponer el dinero en caso se haya realizado el pago." +
                        "Gracias por su comprensión, esperamos contar con su preferencia siempre.";
                customEmailService.sendMailToUsersMensaje(listaInscritos,listaClaseEnroll.get(0).getClase(),mensaje);
                ventaCanceladaRepository.saveAll(VentaCancelada.generateVentaCanceladas(ventasDeClase));
                ventaRepository.deleteAll(ventasDeClase);
            }
            claseSesionRepository.deleteAll(clase.getClaseSesions());
            claseEnrollRepository.deleteAll(clase.getClaseEnrollList());
            claseRepository.delete(clase);
            attributes.addFlashAttribute("msgSuccess", "Paquete de Asesorías eliminado correctamente");

        }else {
            attributes.addFlashAttribute("msgError", "No se encontró la clase");
        }
        return "redirect:/admin/ases-paq/";
    }


}
