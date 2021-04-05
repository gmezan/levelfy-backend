package com.uc.backend.controller.android;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.UserRepository;
import com.uc.backend.security.jwt.JwtProvider;
import com.uc.backend.service.model.UserService;
import com.uc.backend.util.EnrollmentVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("android")
public class AndroidController {

    EnrollmentRepository enrollmentRepository;
    ServiceRepository serviceRepository;
    UserService userService;


    @Autowired
    public AndroidController(EnrollmentRepository enrollmentRepository, ServiceRepository serviceRepository, UserService userService) {
        this.enrollmentRepository = enrollmentRepository;
        this.serviceRepository = serviceRepository;
        this.userService=userService;
    }



    @PostMapping(value = "enroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> newEnrollment(@RequestParam(name = "i", required = false) int idClase
        ) {
        return   serviceRepository.findById(idClase)
                .map((service) -> {
                    Enrollment newEnrollment=new Enrollment();
                    newEnrollment.setService(service);
                    return userService.getCurrentUser()
                            .map((user) -> {
                                newEnrollment.setStudent(user);
                                newEnrollment.setPayed(false);
                                newEnrollment.setActive(true);
                                return new ResponseEntity<>(enrollmentRepository.save(newEnrollment), OK);
                            })
                            .orElseGet(() -> new ResponseEntity<>(null, BAD_REQUEST));


                })
                  .orElseGet( ()-> new ResponseEntity<>(null, BAD_REQUEST));



    }


    @GetMapping(value = "verifyEnrollemnt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnrollmentVerify> verifyEnrollment(@RequestParam(name = "i", required = false) int idClase
    ) {
        EnrollmentVerify enrollmentVerify=new EnrollmentVerify();
        return userService.getCurrentUser().map(user ->
                enrollmentRepository.findEnrollmentByService_IdServiceAndStudent_IdUser(idClase,user.getIdUser()).map(enrollment ->{
             enrollmentVerify.setEnroll(true);enrollmentVerify.setIdEnrollment(enrollment.getIdEnrollment());
             return new ResponseEntity<>( enrollmentVerify , OK); }  ).orElseGet(() ->  new ResponseEntity<>( enrollmentVerify , OK)))
                .orElseGet( () -> new ResponseEntity<>(  null, BAD_REQUEST) );


    }


}
