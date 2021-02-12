package com.uc.backend.controller.general;


import com.uc.backend.dto.CourseId;
import com.uc.backend.dto.FileUploadDto;
import com.uc.backend.entity.*;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.*;
import com.uc.backend.service.aws.AwsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("model")
public class CourseController {



    @Autowired
    CourseRepository courseRepository;


    // List available courses
    @GetMapping(value = "course/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getServicesList(
            @RequestParam(name = "serviceType", required = true) String serviceType) {
        return new ResponseEntity<>(courseRepository.findCoursesAvailableByServiceTypeAndAvailableIsTrue(serviceType), HttpStatus.OK);
    }


    // RESTFUL Service: TESTED AND OK

    @GetMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam(name = "u", required = false) String u) {

        if (u!=null && !u.isEmpty()) {
            return new ResponseEntity<>(courseRepository.findCourseByCourseId_University(u), HttpStatus.OK);
        }

        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "course/{i}/{u}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourse(@PathVariable("i") String id, @PathVariable("u") UniversityName university) {
        return courseRepository.findById(new CourseId(id, university))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> newCourse(@RequestBody Course course) {
        return courseRepository.findById(course.getCourseId())
                .map(value -> new ResponseEntity<>(value, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK));
    }

    @PutMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        return courseRepository.findById(course.getCourseId())
                .map(value -> new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

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
