package com.uc.backend.controller.aws;

import com.uc.backend.dto.CourseId;
import com.uc.backend.dto.FileS3ResponseDto;
import com.uc.backend.dto.FileUploadDto;
import com.uc.backend.entity.CommentForum;
import com.uc.backend.entity.Course;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.CourseRepository;
import com.uc.backend.service.aws.AwsResourceService;
import com.uc.backend.service.model.ForumService;
import com.uc.backend.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("s3/comment-forum")
public class CommentForumS3Controller {

    private final String folder = "commentForum";
    AwsResourceService awsResourceService;
    ForumService forumService;
    UserService userService;

    @Autowired
    public CommentForumS3Controller(AwsResourceService awsResourceService,
                                    ForumService forumService,
                                    UserService userService) {
        this.awsResourceService = awsResourceService;
        this.forumService = forumService;
        this.userService = userService;
    }

    @PostMapping(
            path = "{i}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FileUploadDto> uploadResource(@PathVariable("i") int commentId,
                                                           @RequestParam("file") MultipartFile file) {

        //  System.out.println("I am going to upload a file: " + file.getOriginalFilename());

        return userService.getCurrentUser()
                .map(user ->
                    forumService.getById(commentId, user)
                        .map(commentForum -> {
                            try {
                                FileS3ResponseDto resp = awsResourceService.uploadFile(String.valueOf(commentForum.getService().getIdService()), folder, file);
                                commentForum.setFileUrl(resp.getFileUrl());
                                commentForum.setFileName(resp.getFileName());
                                forumService.save(commentForum);
                                return new ResponseEntity<>(new FileUploadDto(
                                        commentForum.getFileUrl(), commentForum.getFileName()), HttpStatus.OK);
                            } catch (IllegalAccessException e) {
                                return new ResponseEntity<>(new FileUploadDto(), HttpStatus.BAD_REQUEST);
                            } catch (IOException e) {
                                return new ResponseEntity<>(new FileUploadDto(), HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                        })
                        .orElseGet(() -> new ResponseEntity<>(new FileUploadDto(), HttpStatus.BAD_REQUEST))
                )
                .orElseGet(() -> new ResponseEntity<>(new FileUploadDto(), HttpStatus.I_AM_A_TEAPOT));

    }

}
