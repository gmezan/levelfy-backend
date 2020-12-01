package com.uc.backend.controller;

import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/c")
public class ComentarioForoController {

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
}
