package com.uc.backend.controller.roles.teach;

import com.uc.backend.entity.ServiceSession;
import com.uc.backend.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/t")
public class TeachController {

    EnrollmentService enrollmentService;
    UserService userService;
    SaleService saleService;
    ServiceService serviceService;
    ServiceSessionService serviceSessionService;

    @Autowired
    public TeachController(EnrollmentService enrollmentService, UserService userService,
                            SaleService saleService, ServiceService serviceService,
                            ServiceSessionService serviceSessionService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.saleService = saleService;
        this.serviceService = serviceService;
        this.serviceSessionService = serviceSessionService;
    }

    @GetMapping(value = "service-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceSession>> getServiceSessionsByServiceId(
            @RequestParam("serviceId") int serviceId) {

        return userService.getCurrentUser()
                .map( user -> serviceService.isTeacherLecturingService(user, serviceId)
                        .map(service -> new ResponseEntity<>(service.getServiceSessionList(),
                                HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                ).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

    }

}
