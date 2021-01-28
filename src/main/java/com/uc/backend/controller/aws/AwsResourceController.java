package com.uc.backend.controller.aws;

import com.uc.backend.dto.CourseId;
import com.uc.backend.dto.FileUploadDto;
import com.uc.backend.entity.Course;
import com.uc.backend.repository.CourseRepository;
import com.uc.backend.service.aws.AwsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("s3/course")
public class AwsResourceController {

    private final String courses = "courses";

    @Autowired
    AwsResourceService awsResourceService;

    @Autowired
    CourseRepository courseRepository;



    @PostMapping(
            path = "{i}/{u}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FileUploadDto> uploadCourseImage(@PathVariable("i")String courseId,
                                                           @PathVariable("u") String university,
                                                           @RequestParam("file")MultipartFile file) {

        Optional<Course> optionalCourse = courseRepository.findById(new CourseId(courseId, university));

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            String url = null;
            try {
                url = awsResourceService.uploadImage(course.idToString(), courses, file);
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

}
