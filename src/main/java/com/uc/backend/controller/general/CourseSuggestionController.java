package com.uc.backend.controller.general;

import com.uc.backend.entity.Course;
import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.entity.User;
import com.uc.backend.repository.CourseSuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("course-suggestion")
public class CourseSuggestionController {
    @Autowired
    CourseSuggestionRepository courseSuggestionRepository;

    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseSuggestion>> getAll(){
        return new ResponseEntity<>(courseSuggestionRepository.findAll(), HttpStatus.OK);
    }

}
