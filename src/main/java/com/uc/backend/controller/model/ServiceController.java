package com.uc.backend.controller.model;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.User;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.RoleName;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.ServiceSessionRepository;
import com.uc.backend.service.model.ServiceService;
import com.uc.backend.service.model.UserService;
import com.uc.backend.service.prices.LevelfyServicePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("model/service")
public class ServiceController {

    ServiceRepository serviceRepository;
    ServiceSessionRepository serviceSessionRepository;
    LevelfyServicePriceRepository levelfyServicePriceRepository;
    UserService userService;
    ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository, ServiceSessionRepository serviceSessionRepository,
                             LevelfyServicePriceRepository levelfyServicePriceRepository, UserService userService,
                             ServiceService serviceService) {
        this.serviceRepository = serviceRepository;
        this.serviceSessionRepository = serviceSessionRepository;
        this.levelfyServicePriceRepository = levelfyServicePriceRepository;
        this.userService = userService;
        this.serviceService = serviceService;
    }

    /*

         Web Service for forms: Sirve para listar los servicios que ofrece
         un "Paquete de Asesoría" o una "Maratón".

     */
    @GetMapping(value = "form", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getListOfServicesForForm(
            @RequestParam("serviceType") LevelfyServiceType serviceType,
            @RequestParam("i") String idCourse,
            @RequestParam("u") UniversityName university) {
        CourseId courseId = new CourseId(idCourse, university);
        return new ResponseEntity<>(
                serviceRepository.findServicesByServiceTypeAndCourse_CourseIdAndAvailableIsTrue(serviceType,courseId),
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


        User currentUser = userService.getCurrentUser().orElse(null);
        if (currentUser==null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        RoleName userRoleName = Objects.requireNonNull(currentUser.getRole().stream().findFirst().orElse(null)).getName();

        switch (userRoleName) {
            case ROLE_MOD: // verify
                serviceList = serviceList.stream().filter(element->element.getCourse().getCourseId().getUniversity()
                        .equals(currentUser.getUniversity())).collect(Collectors.toList());
                break;
            case ROLE_TEACH:
                serviceList = serviceList.stream().filter(element->element.getTeacher().getIdUser()==currentUser.getIdUser()).collect(Collectors.toList());
                break;
            case ROLE_CLIENT:
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (available == null) return new ResponseEntity<>(serviceList, HttpStatus.OK);

        if (available)
            return new ResponseEntity<>(serviceList.stream().filter(Service::getAvailable).collect(Collectors.toList()), HttpStatus.OK);
        else
            return new ResponseEntity<>(serviceList.stream().filter(serv->!serv.getAvailable()).collect(Collectors.toList()), HttpStatus.OK);

    }

    @GetMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> getService(@PathVariable("s") int id) {
        return serviceRepository.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> newService(@RequestBody Service service) {
        return serviceService.createService(service); }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> updateService(@RequestBody Service service) {
        return serviceService.updateService(service);
    }

    @DeleteMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteService(@PathVariable("s") int id) {
        return serviceService.deleteService(id);
    }

}
