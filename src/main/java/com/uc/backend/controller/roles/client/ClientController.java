package com.uc.backend.controller.roles.client;

import com.uc.backend.dto.PaymentDto;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.Sale;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.service.model.EnrollmentService;
import com.uc.backend.service.model.SaleService;
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
    SaleService saleService;

    @Autowired
    public ClientController(EnrollmentService enrollmentService, UserService userService,
                            SaleService saleService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.saleService = saleService;
    }


    @PostMapping(value = "is-enrolled", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> isAlreadyEnrolled(@RequestBody Enrollment enrollment) {
        return userService.getCurrentUser()
                .map(user -> new ResponseEntity<>(enrollmentService.exists(enrollment, user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }


    @GetMapping(value = "enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Enrollment>> getServicesList(
            @RequestParam(value = "serviceType", required = false) LevelfyServiceType serviceType
    ) {

        return userService.getCurrentUser()
                .map(user -> new ResponseEntity<>(enrollmentService.getEnrollments(user, serviceType), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

    }

    @GetMapping(value = "enrollment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> getServiceById(@PathVariable("id") Integer id) {
        return userService.getCurrentUser()
                .map(user -> enrollmentService.getEnrollmentById(user, id)
                        .map( enrollment -> new ResponseEntity<>(enrollment, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

    }


    @PostMapping(value = "enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> postEnrollment(@RequestBody Enrollment enrollment) {

        return userService.getCurrentUser()
                .map(user -> new ResponseEntity<>(enrollmentService.createEnrollment(enrollment, user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

    }

    @DeleteMapping(value = "enrollment/:id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteServiceById(@RequestParam("id") Integer id) {
        return userService.getCurrentUser()
                .map(user -> enrollmentService.getEnrollmentById(user, id)
                        .map( enrollment -> {
                            enrollmentService.deleteEnrollment(enrollment);
                            return new ResponseEntity(null, HttpStatus.OK);
                        })
                        .orElseGet(() -> new ResponseEntity(null, HttpStatus.BAD_REQUEST)))
                .orElseGet(() -> new ResponseEntity(null, HttpStatus.FORBIDDEN));

    }

    @PostMapping(value = "register-payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> registerPayment(@RequestBody PaymentDto paymentDto) {
        return userService.getCurrentUser()
                .map(user -> enrollmentService.exists(paymentDto.getIdService(), user)
                        .map( enrollment ->
                             new ResponseEntity(saleService.registerClientPayment(paymentDto), HttpStatus.OK)
                        )
                        .orElseGet(() -> new ResponseEntity(null, HttpStatus.BAD_REQUEST)))
                .orElseGet(() -> new ResponseEntity(null, HttpStatus.FORBIDDEN));
    }



}
