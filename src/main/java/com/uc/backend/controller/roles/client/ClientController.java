package com.uc.backend.controller.roles.client;

import com.uc.backend.entity.Course;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.User;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.service.general.EnrollmentService;
import com.uc.backend.service.general.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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



}
