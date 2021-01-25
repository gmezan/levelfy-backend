package com.uc.backend.controller.general;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Service;
import com.uc.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("model/service")
public class ServiceController {
    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getAll(){
        return new ResponseEntity<>(serviceRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "form", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getListOfServicesForForm(
            @RequestParam("serviceType") String serviceType,
            @RequestParam("i") String idCourse,
            @RequestParam("u") String university) {
        CourseId courseId = new CourseId(idCourse, university);
        return new ResponseEntity<>(
                serviceRepository.findServiceByServiceTypeAndCourse_CourseIdAndAvailableIsTrue(serviceType,courseId),
                HttpStatus.OK
        );
    }

}
