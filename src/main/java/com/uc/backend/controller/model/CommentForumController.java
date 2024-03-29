package com.uc.backend.controller.model;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.entity.Role;
import com.uc.backend.enums.RoleName;
import com.uc.backend.repository.CommentForumRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.UserRepository;
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

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin
@RestController
@RequestMapping("model/comment-forum")
public class CommentForumController {

    private final Role ADMIN_ROLE = new Role(4, RoleName.ROLE_ADMIN);
    private final Role TEACH_ROLE = new Role(2, RoleName.ROLE_TEACH);

    ForumService forumService;
    UserService userService;
    ServiceService serviceService;
    EnrollmentService enrollmentService;

    @Autowired
    public CommentForumController(ForumService forumService, UserService userService,
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
                .map(user -> {
                    if(user.getRole().contains(ADMIN_ROLE) || user.getRole().contains(TEACH_ROLE) )
                        return new ResponseEntity<>
                                (forumService.listAllByService_ServiceId(serviceId),OK);

                    return enrollmentService.exists(serviceId, user)
                            .map(enrollment ->
                                    new ResponseEntity<>(
                                            forumService.listAllByService(enrollment.getService()), HttpStatus.OK
                                    )
                            )
                            .orElseGet(() ->
                                    new ResponseEntity<>(
                                            new ArrayList<>(), HttpStatus.BAD_REQUEST)
                            );
                    }
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @GetMapping(value = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> getCommentForumById(
            @PathVariable("id") int idComment) {

        final CommentForum mockCommentForum = new CommentForum(idComment);

        return userService.getCurrentUser()
                .map(user -> {
                    if(user.getRole().contains(ADMIN_ROLE))
                        return forumService.getById(mockCommentForum)
                                .map(commentForum -> new ResponseEntity<>(commentForum, HttpStatus.OK))
                                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

                    return forumService.getById(mockCommentForum, user)
                            .map(commentForum -> new ResponseEntity<>(commentForum, HttpStatus.OK))
                            .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
                        }
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @PostMapping(value = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> postCommentForum(
            @RequestBody CommentForum commentForum) {
        return userService.getCurrentUser()
                .map(user ->
                        {
                            if(user.getRole().contains(ADMIN_ROLE))
                                return serviceService.findById(commentForum.getService().getIdService()).
                                        map(service ->
                                                new ResponseEntity<>(forumService.create(commentForum, service, user), OK))
                                        .orElseGet(()->new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));


                            else if (user.getRole().contains(TEACH_ROLE))
                                return serviceService.isTeacherLecturingService(user, commentForum.getService().getIdService()).
                                        map(service ->
                                                new ResponseEntity<>(forumService.create(commentForum, service, user), OK))
                                        .orElseGet(()->new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

                            return enrollmentService.exists(commentForum.getService().getIdService(), user)
                                    .map(enrollment ->
                                            new ResponseEntity<>(
                                                    forumService.create(commentForum, enrollment.getService(), user), HttpStatus.OK
                                            )
                                    )
                                    .orElseGet(() ->
                                            new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)
                                    );
                        }
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @PutMapping(value = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> updateCommentForum(
            @RequestBody CommentForum commentForum) {
        return userService.getCurrentUser()
                .map(user ->
                        {
                            if(user.getRole().contains(ADMIN_ROLE))
                                return forumService.getById(commentForum)
                                        .map(commentForum1 ->
                                                new ResponseEntity<>(forumService.update(commentForum1, commentForum), HttpStatus.OK)
                                        )
                                        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

                            return forumService.getById(commentForum, user)
                                    .map(commentForum1 ->
                                            new ResponseEntity<>(forumService.update(commentForum1, commentForum), HttpStatus.OK)
                                    )
                                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
                        }
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    @DeleteMapping(value = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteForum(
            @PathVariable("id") int idComment) {

        final CommentForum mockCommentForum = new CommentForum(idComment);

        return userService.getCurrentUser()
                .map(user -> {

                        if(user.getRole().contains(ADMIN_ROLE))
                            return forumService.getById(mockCommentForum)
                                    .map(commentForum -> {
                                        forumService.delete(commentForum);
                                        return new ResponseEntity(null, HttpStatus.OK);
                                    })
                                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

                        return forumService.getById(mockCommentForum, user)
                                .map(commentForum -> {
                                    forumService.delete(commentForum);
                                    return new ResponseEntity(null, HttpStatus.OK);
                                })
                                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
                        }
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

}
