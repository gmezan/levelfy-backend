package com.uc.backend.controller.estudiante;

import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.entity.User;
import com.uc.backend.repository.CourseSuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/suggestion")
public class SugerenciaController {

    /*
    @Autowired
    CourseSuggestionRepository courseSuggestionRepository;

    @GetMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sugerenciaCurso(@PathVariable("s") String curso, HttpSession session) {
        User user = (User) session.getAttribute("usuario");
        CourseSuggestion sc = new CourseSuggestion();
        if (user != null) {
            sc.setUsuario(user);
        }
        sc.setName(curso);
        courseSuggestionRepository.save(sc);

        return new ResponseEntity(HttpStatus.OK);
    }


    */
}
