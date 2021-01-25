package com.uc.backend.controller.general;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("enrollment")
public class EnrollmentController {
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Enrollment>> getAll(){
        return new ResponseEntity<>(enrollmentRepository.findAll(), HttpStatus.OK);
    }

}
