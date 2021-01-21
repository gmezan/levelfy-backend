package com.uc.backend.controller.general;


import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.*;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/model/course")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;


    // List available courses
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getServicesList(
            @RequestParam(name = "serviceType", required = true) String serviceType) {
        return new ResponseEntity<>(courseRepository.findCoursesAvailableByServiceTypeAndAvailableIsTrue(serviceType), HttpStatus.OK);
    }



    // RESTFUL Service: TESTED AND OK

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses() {
        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{i}/{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourse(@PathVariable("i") String id, @PathVariable("u") String university) {
        return courseRepository.findById(new CourseId(id, university))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> newCourse(@RequestBody Course course) {
        return courseRepository.findById(course.getCourseId())
                .map(value -> new ResponseEntity<>(value, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK));
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        return courseRepository.findById(course.getCourseId())
                .map(value -> new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "{i}/{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCourse(@PathVariable("i") String id, @PathVariable("u") String university) {
        return courseRepository.findById(new CourseId(id, university))
                .map(value -> {
                    courseRepository.delete(value);
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));
    }

}
