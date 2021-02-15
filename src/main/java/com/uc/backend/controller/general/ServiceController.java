package com.uc.backend.controller.general;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Course;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.ServiceSession;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.ServiceSessionRepository;
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
    ServiceSessionRepository serviceSessionRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository, ServiceSessionRepository serviceSessionRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceSessionRepository = serviceSessionRepository;
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

    // RESTFUL Service -> TODO: Test

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getAll(@RequestParam(name = "u", required = false) UniversityName u,
                                                      @RequestParam(name = "s", required = false) LevelfyServiceType s) {
        if (u!=null && s!=null)
            return new ResponseEntity<>(serviceRepository.findServicesByCourse_CourseId_UniversityAndServiceType(u, s), HttpStatus.OK);
        else if(u!=null)
            return new ResponseEntity<>(serviceRepository.findServicesByCourse_CourseId_University(u), HttpStatus.OK);
        else if(s!=null)
            return new ResponseEntity<>(serviceRepository.findServicesByServiceType(s), HttpStatus.OK);
        return new ResponseEntity<>(serviceRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> getCourse(@PathVariable("s") int id) {
        return serviceRepository.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> newCourse(@RequestBody Service service) {
        return serviceRepository.findById(service.getIdService())
                .map(value -> new ResponseEntity<>(value, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    List<ServiceSession> serviceSessionList = service.getServiceSessionList();
                    Service newService = serviceRepository.save(service);
                    serviceSessionList.forEach(sl->sl.setService(newService));
                    serviceSessionRepository.saveAll(serviceSessionList);
                    return new ResponseEntity<>(newService, HttpStatus.OK);
                });
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> updateCourse(@RequestBody Service service) {
        return serviceRepository.findById(service.getIdService())
                .map(value -> new ResponseEntity<>(serviceRepository.save(service), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCourse(@PathVariable("s") int id) {
        return serviceRepository.findById(id)
                .map(value -> {
                    serviceRepository.delete(value);
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));
    }

}
