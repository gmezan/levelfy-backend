package com.uc.backend.controller.roles.client;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.service.model.EnrollmentService;
import com.uc.backend.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/c")
public class ClientController {

    EnrollmentService enrollmentService;
    UserService userService;

    @Autowired
    public ClientController(EnrollmentService enrollmentService, UserService userService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
    }

    @GetMapping(value = "enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Enrollment>> getServicesList(
            @RequestParam(value = "serviceType", required = false) LevelfyServiceType serviceType
    ) {

        return userService.getCurrentUser()
                .map(user -> new ResponseEntity<>(enrollmentService.getEnrollments(user, serviceType), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

    }

    @GetMapping(value = "enrollment/:id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> getServiceById(@RequestParam("id") Integer id) {
        return userService.getCurrentUser()
                .map(user -> enrollmentService.getEnrollmentById(user, id)
                        .map( enrollment -> new ResponseEntity<>(enrollment, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

    }





}
