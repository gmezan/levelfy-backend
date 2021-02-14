package com.uc.backend.controller.general;

import com.uc.backend.entity.ServiceSession;
import com.uc.backend.repository.ServiceSessionRepository;
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
public class ServiceSessionController {
    @Autowired
    ServiceSessionRepository serviceSessionRepository;
    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceSession>> getAll(){
        return new ResponseEntity<>(serviceSessionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{es}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceSession> getServiceSession(@PathVariable("es") int idServiceSession){
        return serviceSessionRepository.findById(idServiceSession)
                .map( (value) -> new ResponseEntity<>(value,OK))
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceSession> newServiceSession(@RequestBody ServiceSession serviceSession) {
        return serviceSessionRepository.findById(serviceSession.getIdServiceSession())
                .map( (value) -> new ResponseEntity<>(value,BAD_REQUEST))
                .orElseGet( () -> {
                    serviceSession.setService(serviceRepository.findById(serviceSession.getService().getIdService()).get());
                    return new ResponseEntity<>(serviceSessionRepository.save(serviceSession),OK);
                });
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceSession> updateServiceSession(@RequestBody ServiceSession serviceSession) {
        return serviceSessionRepository.findById(serviceSession.getIdServiceSession())
                .map( (value) -> {
                    serviceSession.setService(serviceRepository.findById(serviceSession.getService().getIdService()).get());
                    return new ResponseEntity<>(serviceSessionRepository.save(serviceSession), OK);
                })
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }

    @DeleteMapping(value = "{es}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteServiceSession(@PathVariable("es") int idEnrollmenteSession) {
        return serviceSessionRepository.findById(idEnrollmenteSession)
                .map( (value) -> {
                    serviceSessionRepository.deleteById(idEnrollmenteSession);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( () -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }
}
