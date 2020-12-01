package com.uc.backend.controller.estudiante;

import com.uc.backend.entity.SugerenciaCurso;
import com.uc.backend.entity.Usuario;
import com.uc.backend.repository.SugerenciaCursoRepository;
import com.uc.backend.repository.UsuarioRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.uc.backend.utils.CustomConstants.SERVICIO_ASESORIA_PERSONALIZADA;


@Controller
@RequestMapping("/suggestion")
public class SugerenciaController {
    @Autowired
    UsuarioRepository usuariorepository;
    @Autowired
    SugerenciaCursoRepository sugerenciaCursoRepository;

    // SUGERENCIAS DE CURSOS :
    @ResponseBody
    @GetMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sugerenciaCurso(@PathVariable("s") String curso, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuario");
        SugerenciaCurso sc = new SugerenciaCurso();
        if (user != null) {
            sc.setUsuario(user);
        }
        sc.setNombre(curso);
        sugerenciaCursoRepository.save(sc);

        return new ResponseEntity(HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping(value="/cantinvitmatr/{idusuario}")
    public ResponseEntity cantidadDeInvitadosMatriculados(@PathVariable("idusuario") int idusuario){
        Gson gson = new Gson();
        String invitadosMatriculados=(usuariorepository.findCantidadInvitadosMatriculados(idusuario)==null)?"0":usuariorepository.findCantidadInvitadosMatriculados(idusuario);
        System.out.println(invitadosMatriculados+','+usuariorepository.findInvitados(idusuario));
        return new ResponseEntity(invitadosMatriculados+','+usuariorepository.findInvitados(idusuario),HttpStatus.OK);

    }

}
