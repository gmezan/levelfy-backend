package com.uc.backend.controller.roles.client;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.service.model.EnrollmentService;
import com.uc.backend.service.model.ForumService;
import com.uc.backend.service.model.ServiceService;
import com.uc.backend.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/c/forum")
public class ClientForumController {

    ForumService forumService;
    UserService userService;
    ServiceService serviceService;
    EnrollmentService enrollmentService;

    @Autowired
    public ClientForumController(ForumService forumService, UserService userService,
                                 ServiceService serviceService, EnrollmentService enrollmentService) {
        this.forumService = forumService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.enrollmentService = enrollmentService;
    }


    @GetMapping(value = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentForum>> getAllByServiceId(
            @RequestParam("serviceId") int serviceId
    ) {

        return userService.getCurrentUser()
                .map(user ->
                    enrollmentService.exists(serviceId, user)
                            .map(enrollment ->
                                    new ResponseEntity<>(
                                            forumService.listAll(enrollment.getService(), user), HttpStatus.OK
                                    )
                            )
                            .orElseGet(() ->
                                    new ResponseEntity<>(
                                            new ArrayList<>(), HttpStatus.BAD_REQUEST)
                            )
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @GetMapping(value = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> getCommentForumById(
            @PathVariable("id") int id) {
        return userService.getCurrentUser()
                .map(user -> forumService.getById(id, user)
                        .map(commentForum ->
                                new ResponseEntity<>(forumService.create(commentForum), HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @PostMapping(value = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> postCommentForum(
            @RequestBody CommentForum commentForum) {
        return userService.getCurrentUser()
                .map(user ->
                        enrollmentService.exists(commentForum.getService().getIdService(), user)
                                .map(enrollment ->
                                        new ResponseEntity<>(
                                                forumService.create(commentForum), HttpStatus.OK
                                        )
                                )
                                .orElseGet(() ->
                                        new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)
                                )
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @DeleteMapping(value = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteForum(
            @PathVariable("id") int id) {

        return userService.getCurrentUser()
                .map(user -> forumService.getById(id, user)
                        .map(commentForum -> {
                            forumService.delete(commentForum);
                            return new ResponseEntity(null, HttpStatus.OK);
                        })
                        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

}
