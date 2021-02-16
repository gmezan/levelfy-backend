package com.uc.backend.controller.general;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Course;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.ServiceSession;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.ServiceSessionRepository;
import com.uc.backend.service.prices.LevelfyServicePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("model/service")
public class ServiceController {

    ServiceRepository serviceRepository;
    ServiceSessionRepository serviceSessionRepository;
    LevelfyServicePriceRepository levelfyServicePriceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository, ServiceSessionRepository serviceSessionRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceSessionRepository = serviceSessionRepository;
        this.levelfyServicePriceRepository = levelfyServicePriceRepository;
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

    @GetMapping(value = "get-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPrices() {
        return new ResponseEntity(
                levelfyServicePriceRepository.getAllPrices(),
                HttpStatus.OK
        );
    }

    // RESTFUL Service -> TODO: Test

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getAll(@RequestParam(name = "u", required = false) UniversityName u,
                                                      @RequestParam(name = "s", required = false) LevelfyServiceType s,
                                                @RequestParam(name = "a", required = false) Boolean available ) {
        List<Service> serviceList;

        if (u!=null && s!=null) serviceList = serviceRepository.findServicesByCourse_CourseId_UniversityAndServiceType(u, s);
        else if(u!=null) serviceList = serviceRepository.findServicesByCourse_CourseId_University(u);
        else if(s!=null) serviceList = serviceRepository.findServicesByServiceType(s);
        else serviceList = serviceRepository.findAll();

        if (available == null) return new ResponseEntity<>(serviceList, HttpStatus.OK);

        if (available)
            return new ResponseEntity<>(serviceList.stream().filter(Service::getAvailable).collect(Collectors.toList()), HttpStatus.OK);
        else
            return new ResponseEntity<>(serviceList.stream().filter(serv->!serv.getAvailable()).collect(Collectors.toList()), HttpStatus.OK);

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
                    serviceSessionRepository.deleteAll(value.getServiceSessionList());
                    serviceRepository.delete(value);
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));
    }

}
