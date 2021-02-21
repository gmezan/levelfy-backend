package com.uc.backend.controller.general;

import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.repository.CourseSuggestionRepository;
import com.uc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Enum.valueOf;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin
@RestController
@RequestMapping("model/course-suggestion")
public class CourseSuggestionController {
    @Autowired
    CourseSuggestionRepository courseSuggestionRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseSuggestion>> getAll(){
        return new ResponseEntity<>(courseSuggestionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{cs}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseSuggestion> getCourseSuggestion(@PathVariable("cs") int idSuggestion) {
        return courseSuggestionRepository.findById(idSuggestion)
                .map( (value) -> new ResponseEntity<>(value,OK))
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }

    @PostMapping(value = "open",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseSuggestion> newCourseSuggestion(@RequestBody CourseSuggestion courseSuggestion) {

        if (!(courseSuggestion.getUser() != null &&
            userRepository.findById(courseSuggestion.getUser().getIdUser()).isPresent())
        ) {
            courseSuggestion.setUser(null);
        }

        return courseSuggestionRepository.findById(courseSuggestion.getIdSuggestion())
                .map( (value) -> new ResponseEntity<>(value, BAD_REQUEST))
                .orElseGet( () ->
                         new ResponseEntity<>(courseSuggestionRepository.save(courseSuggestion),OK)
                );
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseSuggestion> updateCourseSuggestion(@RequestBody CourseSuggestion courseSuggestion) {
        return courseSuggestionRepository.findById(courseSuggestion.getIdSuggestion())
                .map( (value) ->
                        userRepository.findById(courseSuggestion.getUser().getIdUser())
                                .map( (user) -> {
                                    courseSuggestion.setUser(user);
                                    return new ResponseEntity<>(courseSuggestionRepository.save(courseSuggestion),OK);
                                })
                                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST))
                )
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }

    @DeleteMapping(value = "{cs}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCourseSuggestion(@PathVariable("cs") int idSuggestion) {
        return courseSuggestionRepository.findById(idSuggestion)
                .map( (value) -> {
                    courseSuggestionRepository.deleteById(idSuggestion);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( () -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }
}
