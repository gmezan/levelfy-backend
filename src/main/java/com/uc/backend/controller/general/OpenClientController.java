package com.uc.backend.controller.general;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Course;
import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.entity.Service;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.service.model.CourseService;
import com.uc.backend.service.model.CourseSuggestionService;
import com.uc.backend.service.model.ServiceService;
import com.uc.backend.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/open")
public class OpenClientController {

    UserService userService;
    CourseSuggestionService courseSuggestionService;
    CourseService courseService;
    ServiceService serviceService;

    @Autowired
    public OpenClientController(UserService userService,
                                CourseSuggestionService courseSuggestionService,
                                CourseService courseService,
                                ServiceService serviceService) {
        this.userService = userService;
        this.courseSuggestionService = courseSuggestionService;
        this.courseService = courseService;
        this.serviceService = serviceService;
    }

    @PostMapping(value = "course-suggestion",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseSuggestion> newCourseSuggestion(@RequestBody CourseSuggestion courseSuggestion) {

        if (!(courseSuggestion.getUser() != null && userService.findById(courseSuggestion.getUser().getIdUser()).isPresent())
        ) courseSuggestion.setUser(null);

        return courseSuggestionService.getById(courseSuggestion.getIdSuggestion())
                .map( (value) -> new ResponseEntity<>(value, BAD_REQUEST) )
                .orElseGet( () -> new ResponseEntity<>(courseSuggestionService.save(courseSuggestion),OK));
    }

    // List available services -----------------------

    @GetMapping(value = "service/list-by-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCoursesListByService(
            @RequestParam("serviceType") LevelfyServiceType serviceType) {
        return new ResponseEntity<>(courseService.getCoursesOfAvailableServicesByServiceType(serviceType), HttpStatus.OK);
    }

    @GetMapping(value = "service/list-by-teach", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getTeachListByService(
            @RequestParam("serviceType") LevelfyServiceType serviceType) {
        return new ResponseEntity<>(courseService.getCoursesOfAvailableServicesByServiceType(serviceType), HttpStatus.OK);
    }

    // ----------------------------------------------------------



    // Services Forms --------------

    /*
        Web Service for forms: Sirve para listar los servicios que ofrece
        un "Paquete de Asesoría" o una "Maratón".
    */
    @GetMapping(value = "service/form-by-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getListOfServicesForFormByCourse(
            @RequestParam("serviceType") LevelfyServiceType serviceType,
            @RequestParam("i") String idCourse,
            @RequestParam("u") UniversityName university) {
        CourseId courseId = new CourseId(idCourse, university);
        return new ResponseEntity<>(serviceService.getServiceFormByCourse(serviceType,courseId),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "service/form-by-teach", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Service>> getListOfServicesForFormByTeach(
            @RequestParam("serviceType") LevelfyServiceType serviceType,
            @RequestParam("i") String idCourse,
            @RequestParam("u") UniversityName university) {
        CourseId courseId = new CourseId(idCourse, university);
        return new ResponseEntity<>(serviceService.getServiceFormByCourse(serviceType,courseId),
                HttpStatus.OK
        );
    }


    // -----------------------------



}
