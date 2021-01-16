package com.uc.backend.controller.general;


import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeacherAvailabilityRepository teacherAvailabilityRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentSessionRepository enrollmentSessionRepository;

    @Autowired
    CommentForumRepository commentForumRepository;


    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAll(){
        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }



}
