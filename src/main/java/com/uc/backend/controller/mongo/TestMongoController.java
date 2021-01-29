package com.uc.backend.controller.mongo;

import com.uc.backend.repository.mongo.MongoCourseRepository;
import com.uc.backend.service.mongo.MongoCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("mongo")
public class TestMongoController {
    @Autowired
    MongoCourseRepository mongoCourseRepository;

    @GetMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MongoCourse>> getCourses() {
        return new ResponseEntity<>(mongoCourseRepository.findAll(), HttpStatus.OK
        );
    }

}
