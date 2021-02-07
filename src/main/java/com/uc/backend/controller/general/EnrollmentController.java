package com.uc.backend.controller.general;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("model/enrollment")
public class EnrollmentController {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Enrollment>> getAll(){
        return new ResponseEntity<>(enrollmentRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{e}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> getEnrollment(@PathVariable("e") int enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .map( (value) -> new ResponseEntity<>(value, OK))
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> newEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentRepository.findById(enrollment.getIdEnrollment())
                .map( (value) -> new ResponseEntity<>(value,BAD_REQUEST))
                .orElseGet( () -> {
                    enrollment.setService(serviceRepository.findById(enrollment.getService().getIdService()).get());
                    return new ResponseEntity<>(enrollmentRepository.save(enrollment),OK);
                });
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> updateEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentRepository.findById(enrollment.getIdEnrollment())
                .map( (value) -> {
                    enrollment.setService(serviceRepository.findById(enrollment.getService().getIdService()).get());
                    return new ResponseEntity<>(enrollmentRepository.save(enrollment), OK);
                })
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @DeleteMapping(value = "{e}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEnrollment(@PathVariable("e") int enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .map( (value) -> {
                    enrollmentRepository.deleteById(enrollmentId);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( ()-> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }

}
