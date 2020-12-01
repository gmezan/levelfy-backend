package com.uc.backend.controller.asesor;

import com.uc.backend.entity.Clase;
import com.uc.backend.entity.Usuario;
import com.uc.backend.repository.ClaseRepository;
import com.uc.backend.repository.ClaseSesionRepository;
import com.uc.backend.repository.CursoRepository;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static com.uc.backend.utils.CustomConstants.SERVICIO_ASESORIA_PERSONALIZADA;
import static com.uc.backend.utils.CustomConstants.SERVICIO_SELF_PACED;


@Controller
@RequestMapping("a/self-p")
public class CrudSelfPController {
    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired private Scheduler scheduler;

    @GetMapping("info/{id}")
    public String infoSelfP(@PathVariable("id") int id, Model model,
                              HttpSession session, RedirectAttributes attributes){

        return "asesor/self-p/infoSelfP";
    }

    // ------ METODOS PARA EL CRUD ------

    @GetMapping("edit/{id}")
    public String editSelfP(@PathVariable("id") int id, Model model,
                              HttpSession session, RedirectAttributes attributes){

        return "asesor/self-p/formSelfP";
    }

    @GetMapping("new")
    public String formSelfP(@ModelAttribute("clase") Clase clase, HttpSession session, Model model){
        Usuario user = (Usuario)session.getAttribute("usuario");
        model.addAttribute("clase",clase);
        model.addAttribute("listaCursos",cursoRepository.findAllByCursoId_Universidad(user.getUniversidad()));
        return "asesor/self-p/formSelfP";
    }

    @GetMapping("list")
    public String listSelfP(Model model, HttpSession session){
        Usuario user = (Usuario)session.getAttribute("usuario");
        model.addAttribute("listAsesPer", claseRepository
                .findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(user.getIdusuario(),SERVICIO_SELF_PACED));
        return "asesor/self-p/listSelfP";
    }

    // Para editar y nuevo
    @PostMapping("save")
    public String saveSelfP(@ModelAttribute("clase") Clase clase, HttpSession session, Model model,
                              RedirectAttributes attributes){

        return "asesor/self-p/listSelfP";
    }

    @GetMapping("delete/{id}")
    public String deleteSelfP(@PathVariable("id") int id, HttpSession session, RedirectAttributes attributes){

        return "asesor/self-p/listSelfP";
    }



}
