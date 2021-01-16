package com.uc.backend.controller.general;

import com.uc.backend.entity.Course;
import com.uc.backend.entity.Service;
import com.uc.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("service")
public class ServiceController {
    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getAll(){
        return new ResponseEntity<>(serviceRepository.findAll(), HttpStatus.OK);
    }

}
