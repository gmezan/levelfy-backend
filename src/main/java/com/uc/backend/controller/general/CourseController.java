package com.uc.backend.controller.general;


import com.uc.backend.dto.CourseId;
import com.uc.backend.dto.FileUploadDto;
import com.uc.backend.entity.*;
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
@RequestMapping("/model/course")
public class CourseController {

    private final String coursesS3FolderName = "courses";

    @Autowired
    AwsResourceService awsResourceService;


    @Autowired
    CourseRepository courseRepository;


    // List available courses
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getServicesList(
            @RequestParam(name = "serviceType", required = true) String serviceType) {
        return new ResponseEntity<>(courseRepository.findCoursesAvailableByServiceTypeAndAvailableIsTrue(serviceType), HttpStatus.OK);
    }

    // List all courses by university
    @GetMapping(value = "univ", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCoursesListByUniversity(
            @RequestParam(name = "u", required = true) String u) {
        return new ResponseEntity<>(courseRepository.findCourseByCourseId_University(u), HttpStatus.OK);
    }

    // To Upload an Image attached to the course
    @PostMapping(path = "image-upload/i}/{u}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Access-Control-Allow-Origin: *"})
    public ResponseEntity<FileUploadDto> uploadCourseImage(@PathVariable("i")String courseId,
                                                           @PathVariable("u") String university,
                                                           @RequestParam("file") MultipartFile file) {
        Optional<Course> optionalCourse = courseRepository.findById(new CourseId(courseId, university));

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            String url = null;
            try {
                url = awsResourceService.uploadImage(course.idToString(), coursesS3FolderName, file);
                course.setPhoto(url);
                courseRepository.save(course);
                return new ResponseEntity<>(new FileUploadDto(url), HttpStatus.OK);
            } catch (IllegalAccessException e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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

    // PatchMapping?



}
