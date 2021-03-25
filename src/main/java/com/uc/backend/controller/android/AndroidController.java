package com.uc.backend.controller.android;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.UserRepository;
import com.uc.backend.security.jwt.JwtProvider;
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
    UserRepository userRepository;


    @Autowired
    public AndroidController(EnrollmentRepository enrollmentRepository, ServiceRepository serviceRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.serviceRepository = serviceRepository;
    }



    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> newEnrollment(@RequestParam(name = "i", required = false) int idClase
        ) {
        Enrollment newEnrollment=new Enrollment();
        JwtProvider jwtProvider=new JwtProvider();

          serviceRepository.findById(idClase)
                .map((service) -> {
                    newEnrollment.setService(service);
                    userRepository.findByEmail("jlopezc@pucp.edu.pe")
                            .map((user) -> {
                                newEnrollment.setStudent(user);
                                return new ResponseEntity<>(enrollmentRepository.save(newEnrollment), OK);
                            })
                            .orElseGet(() -> new ResponseEntity<>(null, BAD_REQUEST));
                 return new ResponseEntity<>(null, BAD_REQUEST);

                })
                  .orElseGet( ()->new ResponseEntity<>(null, BAD_REQUEST));


        return new ResponseEntity<>(null, BAD_REQUEST);
    }



    @DeleteMapping(value = "enrollment/{e}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEnrollment(@PathVariable("e") int enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .map( (value) -> {
                    enrollmentRepository.deleteById(enrollmentId);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( ()-> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }

}
