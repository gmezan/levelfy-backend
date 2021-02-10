package com.uc.backend.controller.general;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.EnrollmentSession;
import com.uc.backend.repository.EnrollmentSessionRepository;
import com.uc.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("model/enrollment-session")
public class EnrollmentSessionController {
    @Autowired
    EnrollmentSessionRepository enrollmentSessionRepository;
    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentSession>> getAll(){
        return new ResponseEntity<>(enrollmentSessionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{es}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnrollmentSession> getEnrollmentSession(@PathVariable("es") int idEnrollmentSession){
        return enrollmentSessionRepository.findById(idEnrollmentSession)
                .map( (value) -> new ResponseEntity<>(value,OK))
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnrollmentSession> newEnrollmentSession(@RequestBody EnrollmentSession enrollmentSession) {
        return enrollmentSessionRepository.findById(enrollmentSession.getIdEnrollmentSession())
                .map( (value) -> new ResponseEntity<>(value,BAD_REQUEST))
                .orElseGet( () -> {
                    enrollmentSession.setService(serviceRepository.findById(enrollmentSession.getService().getIdService()).get());
                    return new ResponseEntity<>(enrollmentSessionRepository.save(enrollmentSession),OK);
                });
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnrollmentSession> updateEnrollmentSession(@RequestBody EnrollmentSession enrollmentSession) {
        return enrollmentSessionRepository.findById(enrollmentSession.getIdEnrollmentSession())
                .map( (value) -> {
                    enrollmentSession.setService(serviceRepository.findById(enrollmentSession.getService().getIdService()).get());
                    return new ResponseEntity<>(enrollmentSessionRepository.save(enrollmentSession), OK);
                })
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }

    @DeleteMapping(value = "{es}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEnrollmentSession(@PathVariable("es") int idEnrollmenteSession) {
        return enrollmentSessionRepository.findById(idEnrollmenteSession)
                .map( (value) -> {
                    enrollmentSessionRepository.deleteById(idEnrollmenteSession);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( () -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }
}
