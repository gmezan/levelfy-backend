package com.uc.backend.controller.client;

import org.springframework.web.bind.annotation.*;


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
