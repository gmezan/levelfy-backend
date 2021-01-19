package com.uc.backend.controller.general;


import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;


    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAll(){
        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<Course>> getServicesList(
            @RequestParam(name = "serviceType", required = true) String serviceType) {
        return new ResponseEntity<>(courseRepository.findCoursesAvailableByServiceType(serviceType), HttpStatus.OK);
    }



}
