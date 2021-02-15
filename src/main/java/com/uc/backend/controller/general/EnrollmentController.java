package com.uc.backend.controller.general;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("model/enrollment")
public class EnrollmentController {

    EnrollmentRepository enrollmentRepository;
    ServiceRepository serviceRepository;

    @Autowired
    public EnrollmentController(EnrollmentRepository enrollmentRepository, ServiceRepository serviceRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Enrollment>> getAll(@RequestParam(name = "u", required = false) UniversityName u,
                                                   @RequestParam(name = "s", required = false) LevelfyServiceType s,
                                                   @RequestParam(name = "a", required = false) Boolean active) {
        List<Enrollment> enrollmentList;
        if (u!=null && s!=null)
            enrollmentList = enrollmentRepository.findEnrollmentsByService_ServiceTypeAndService_Course_CourseId_University(s, u);
        else if(u!=null)
            enrollmentList = enrollmentRepository.findEnrollmentsByService_Course_CourseId_University(u);
        else if(s!=null)
            enrollmentList = enrollmentRepository.findEnrollmentsByService_ServiceType(s);
        else enrollmentList = enrollmentRepository.findAll();

        if (active == null) return new ResponseEntity<>(enrollmentList, OK);

        if (active) return new ResponseEntity<>(enrollmentList.stream().filter(Enrollment::getActive).collect(Collectors.toList()), OK);
        else return new ResponseEntity<>(enrollmentList.stream().filter(e->!e.getActive()).collect(Collectors.toList()), OK);

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
