package com.uc.backend.controller.estudiante;


import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/curso")
public class CursoController {

    String rutaAbsoluta = "/home/ubuntu/fotos/";

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

    private String[] metodosDePago = new String[]{"Yape", "Plin", "Lukitas", "Depósito a cuenta BCP", "Depósito a cuenta BBVA"};
    @ResponseBody
    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Curso>> getAll(){
        return new ResponseEntity<>(cursoRepository.findAll(), HttpStatus.OK);
    }



}
