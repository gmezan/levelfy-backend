package com.uc.backend.controller.general;

import com.uc.backend.entity.Course;
import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.service.model.CourseService;
import com.uc.backend.service.model.CourseSuggestionService;
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

    @Autowired
    public OpenClientController(UserService userService,
                                CourseSuggestionService courseSuggestionService,
                                CourseService courseService) {
        this.userService = userService;
        this.courseSuggestionService = courseSuggestionService;
        this.courseService = courseService;
    }

    @PostMapping(value = "course-suggestion",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseSuggestion> newCourseSuggestion(@RequestBody CourseSuggestion courseSuggestion) {

        if (!(courseSuggestion.getUser() != null && userService.findById(courseSuggestion.getUser().getIdUser()).isPresent())
        ) courseSuggestion.setUser(null);

        return courseSuggestionService.getById(courseSuggestion.getIdSuggestion())
                .map( (value) -> new ResponseEntity<>(value, BAD_REQUEST) )
                .orElseGet( () -> new ResponseEntity<>(courseSuggestionService.save(courseSuggestion),OK));
    }

    // List available service by-course
    @GetMapping(value = "service/list-by-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCoursesList(
            @RequestParam("serviceType") LevelfyServiceType serviceType) {
        return new ResponseEntity<>(courseService.getCoursesOfAvailableServicesByServiceType(serviceType), HttpStatus.OK);
    }




}
