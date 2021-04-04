package com.uc.backend.controller.model;


import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.*;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("model")
public class CourseController {

    CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    // RESTFUL Service: TESTED AND OK

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MOD') or hasRole('ROLE_TEACH')")
    @GetMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam(name = "u", required = false) UniversityName u) {

        if (u!=null) {
            return new ResponseEntity<>(courseRepository.findCourseByCourseId_University(u), HttpStatus.OK);
        }

        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MOD')")
    @GetMapping(value = "course/{i}/{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourse(@PathVariable("i") String id, @PathVariable("u") UniversityName university) {
        return courseRepository.findById(new CourseId(id, university))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MOD')")
    @PostMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> newCourse(@RequestBody Course course) {
        return courseRepository.findById(course.getCourseId())
                .map(value -> new ResponseEntity<>(value, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MOD')")
    @PutMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        return courseRepository.findById(course.getCourseId())
                .map(value -> new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MOD')")
    @DeleteMapping(value = "course/{i}/{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCourse(@PathVariable("i") String id, @PathVariable("u") UniversityName university) {
        return courseRepository.findById(new CourseId(id, university))
                .map(value -> {
                    courseRepository.delete(value);
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));
    }

    // PatchMapping?



}
