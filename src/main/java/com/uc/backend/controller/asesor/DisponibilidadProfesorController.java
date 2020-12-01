package com.uc.backend.controller.asesor;

import com.uc.backend.entity.DisponibilidadProfesor;
import com.uc.backend.entity.Usuario;
import com.uc.backend.repository.ClaseEnrollRepository;
import com.uc.backend.repository.DisponibilidadProfesorRepository;
import com.uc.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.uc.backend.utils.CustomConstants.DIA;
import static com.uc.backend.utils.CustomConstants.SERVICIO_ASESORIA_PERSONALIZADA;

@Controller
public class DisponibilidadProfesorController {

    @Autowired
    DisponibilidadProfesorRepository disponibilidadProfesorRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    private final int ROL_ASESOR = 2;


    @GetMapping("a/availability")
    public String disponibilidadList(Model model, HttpSession session){
        model.addAttribute("availability", disponibilidadProfesorRepository
                .findDisponibilidadProfesorsByUsuario_IdusuarioAndUsuario_ActivoIsTrueAndUsuario_Rol_Idrol
                        (((Usuario)session.getAttribute("usuario")).getIdusuario(), ROL_ASESOR));
        return "asesor/disponibilidad/list";
    }

    @GetMapping("a/availability/new")
    public String disponibilidadNew(@ModelAttribute("disp") DisponibilidadProfesor disponibilidadProfesor,
            Model model, HttpSession session){
        Usuario user = (Usuario)session.getAttribute("usuario");
        model.addAttribute("dias", DIA);
        model.addAttribute("disp", disponibilidadProfesor);
        model.addAttribute("titleForm","Nueva Disponibilidad");
        return "asesor/disponibilidad/form";
    }


    @GetMapping("a/availability/edit/{id}")
    public String disponibilidadEdit(@ModelAttribute("disp") DisponibilidadProfesor disponibilidadProfesor,
                                     Model model, @PathVariable("id") Integer id, HttpSession session,
                                     RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<DisponibilidadProfesor> optionalDisponibilidadProfesor =
                disponibilidadProfesorRepository.findDisponibilidadProfesorByIdAndUsuario_Idusuario(id,user.getIdusuario());


        if (optionalDisponibilidadProfesor.isPresent()){
            disponibilidadProfesor = optionalDisponibilidadProfesor.get();
            model.addAttribute("dias", DIA);
            model.addAttribute("disp", disponibilidadProfesor);
            model.addAttribute("titleForm","Editar Disponibilidad");
            return "asesor/disponibilidad/form";
        }
        else {
            attributes.addFlashAttribute("msgError","No se encontro la disponibilidad");
        }

        return "redirect:/a/availability";
    }


    @PostMapping("a/availability/save")
    public String disponibilidadSave(@ModelAttribute("disp") @Valid DisponibilidadProfesor disponibilidadProfesor,
                                     BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");

        if (disponibilidadProfesor.getInicio()!=null && disponibilidadProfesor.getFin()!=null
                && disponibilidadProfesor.getInicio().isAfter(disponibilidadProfesor.getFin()))
            bindingResult.rejectValue("fin", "error.user", "Elija una fecha de fin válida");

        if (disponibilidadProfesor.getId()==0 && !disponibilidadProfesorRepository.findDisponibilidadProfesorsByUsuario_IdusuarioAndDia
                (user.getIdusuario(), disponibilidadProfesor.getDia()).isEmpty())
            bindingResult.rejectValue("dia","error.user","Este dia no puede tener más de un registro de disponibilidad");

        if (disponibilidadProfesor.getId()!=0 && !disponibilidadProfesorRepository.findDisponibilidadProfesorsByUsuario_IdusuarioAndDia
                (user.getIdusuario(), disponibilidadProfesor.getDia()).isEmpty()
        && !disponibilidadProfesorRepository.findDisponibilidadProfesorsByIdAndUsuario_IdusuarioAndDia
                (disponibilidadProfesor.getId(), user.getIdusuario(), disponibilidadProfesor.getDia()).isPresent())
            bindingResult.rejectValue("dia","error.user","Este dia no puede tener más de un registro de disponibilidad");


        if (!bindingResult.hasErrors()){
            if (disponibilidadProfesor.getId()==0){ //Si es nuevo
                disponibilidadProfesor.setUsuario(user);
                disponibilidadProfesor.setOcupado(false);
                attributes.addFlashAttribute("msgSuccess","Disponibilidad creada correctamente");
            }else {
                Optional<DisponibilidadProfesor> optionalDisponibilidadProfesor =
                        disponibilidadProfesorRepository.findDisponibilidadProfesorByIdAndUsuario_Idusuario
                                (disponibilidadProfesor.getId(), user.getIdusuario());
                if (!optionalDisponibilidadProfesor.isPresent()){
                    attributes.addFlashAttribute("msgError", "Hubo un error");
                    return "redirect:/a/availability";
                }
                else {
                    DisponibilidadProfesor dispTemp = optionalDisponibilidadProfesor.get();
                    dispTemp.setDia(disponibilidadProfesor.getDia());
                    dispTemp.setInicio(disponibilidadProfesor.getInicio());
                    dispTemp.setFin(disponibilidadProfesor.getFin());
                    disponibilidadProfesor = dispTemp;
                }
                attributes.addFlashAttribute("msgSuccess","Disponibilidad actualizada correctamente");
            }
            disponibilidadProfesorRepository.save(disponibilidadProfesor);

        }else {
            model.addAttribute("dias", DIA);
            model.addAttribute("disp", disponibilidadProfesor);
            model.addAttribute("titleForm","Formulario");
            return "asesor/disponibilidad/form";
        }
        return "redirect:/a/availability";
    }


    @GetMapping("a/availability/delete/{id}")
    public String disponibilidadDelete(Model model, @PathVariable("id") Integer id, HttpSession session,
                                       RedirectAttributes attributes){
        Usuario user = (Usuario)session.getAttribute("usuario");
        Optional<DisponibilidadProfesor> optionalDisponibilidadProfesor =
                disponibilidadProfesorRepository.findDisponibilidadProfesorByIdAndUsuario_Idusuario(id,user.getIdusuario());
        if (optionalDisponibilidadProfesor.isPresent()){
            disponibilidadProfesorRepository.delete(optionalDisponibilidadProfesor.get());
            attributes.addFlashAttribute("msgSuccess","Disponibilidad borrada correctamente");
        }
        else {
            attributes.addFlashAttribute("msgError","No se encontro la disponibilidad");
        }

        return "redirect:/a/availability";
    }






    // ------------------------------------------------------- WEB SERVICES -----------------------------------------------------

    @ResponseBody
    @GetMapping(value = "/dev/disponibilidadAsesor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DisponibilidadProfesor>> getDisp(@PathVariable("id") int id){
        return new ResponseEntity<>(disponibilidadProfesorRepository
                .findDisponibilidadProfesorsByUsuario_IdusuarioAndUsuario_ActivoIsTrueAndUsuario_Rol_Idrol(id, ROL_ASESOR), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/dev/disponibilidadAsesor/a/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getMap(@PathVariable("id") int id){
        Optional<Usuario> optionalUsuario = usuarioRepository.findByIdusuarioAndRol_IdrolAndActivoIsTrue(id,ROL_ASESOR);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<LocalDate,ArrayList<LocalTime>> map = new HashMap<>();
        ArrayList<LocalDate> dtProgramado = new ArrayList<>();
        optionalUsuario.ifPresent(user -> claseEnrollRepository.
                findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClaseDisponibleIsTrueAndInicioasesoriaIsAfter(
                        user.getIdusuario(), SERVICIO_ASESORIA_PERSONALIZADA, LocalDateTime.now().minusHours(5))
                .forEach((claseEnroll1) -> {
                    dtProgramado.add(claseEnroll1.getInicioasesoria().toLocalDate());
                }));
        final int[] i = {1};
        if(optionalUsuario.isPresent()){
            Usuario asesor = optionalUsuario.get();
                for (LocalDateTime dp : asesor.mapDisponibilidad().keySet()){
                if (!dtProgramado.contains(dp.toLocalDate())){
                    list.add(new HashMap<String,Object>(){{
                        put("start",dp);
                        put("end",dp.plusHours(1));
                        put("text","Disponible");
                        put("id", Integer.toString(i[0]++));

                        if (!map.containsKey(dp.toLocalDate())) map.put(dp.toLocalDate(), new ArrayList<>());
                        map.get(dp.toLocalDate()).add(dp.toLocalTime());
                    }});
                }
            }
        }
        return new ResponseEntity<>(new HashMap<String, Object>(){{
            put("cal", list);
            put("select", new HashMap<LocalDate,ArrayList<LocalTime>>(){{
                ArrayList<LocalDate> newAL = new ArrayList<>(map.keySet());
                Collections.sort(newAL);
                for (LocalDate ld : newAL){
                    put(ld, map.get(ld));
                    Collections.sort(map.get(ld));
                }
            }});
        }},HttpStatus.OK);
    }

}
