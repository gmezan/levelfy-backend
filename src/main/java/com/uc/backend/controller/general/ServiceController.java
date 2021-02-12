package com.uc.backend.controller.general;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Service;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
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

    ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Web Service for forms
    @GetMapping(value = "form", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getListOfServicesForForm(
            @RequestParam("serviceType") LevelfyServiceType serviceType,
            @RequestParam("i") String idCourse,
            @RequestParam("u") UniversityName university) {
        CourseId courseId = new CourseId(idCourse, university);
        return new ResponseEntity<>(
                serviceRepository.findServiceByServiceTypeAndCourse_CourseIdAndAvailableIsTrue(serviceType,courseId),
                HttpStatus.OK
        );
    }

}
