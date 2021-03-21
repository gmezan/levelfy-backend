package com.uc.backend.controller.model;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.repository.CommentForumRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("model/comment-forum")
public class CommentForumController {
    @Autowired
    CommentForumRepository commentForumRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentForum>> getAll(){
        return new ResponseEntity<>(commentForumRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{cf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> getComment(@PathVariable("cf") int idComment) {
        return commentForumRepository.findById(idComment)
                .map( (value) -> new ResponseEntity<>(value,OK))
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)       // getIdUser
    public ResponseEntity<CommentForum> newComment(@RequestBody CommentForum commentForum) {
        return commentForumRepository.findById(commentForum.getIdComment())
                .map( (value) -> new ResponseEntity<>(value,BAD_REQUEST))
                .orElseGet( () ->
                    serviceRepository.findById(commentForum.getService().getIdService())
                            .map( (service) ->
                                    userRepository.findById(commentForum.getUser().getIdUser())
                                            .map( (user) -> {
                                                commentForum.setService(service);
                                                commentForum.setUser(user);
                                                return  new ResponseEntity<>( commentForumRepository.save(commentForum),OK);
                                            })
                                            .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST)))
                            .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST))
                );
    }

    @PutMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> updateComment(@RequestBody CommentForum commentForum) {
        return commentForumRepository.findById(commentForum.getIdComment())
                .map( (value) ->
                        serviceRepository.findById(commentForum.getService().getIdService())
                                .map( (service) ->
                                        userRepository.findById(commentForum.getUser().getIdUser())
                                                .map( (user) -> {
                                                    commentForum.setService(service);
                                                    commentForum.setUser(user);
                                                    return new ResponseEntity<>(commentForumRepository.save(commentForum),OK);
                                                })
                                                .orElseGet(() -> new ResponseEntity<>(null, BAD_REQUEST)))
                                .orElseGet(() -> new ResponseEntity<>(null,BAD_REQUEST)))
                .orElseGet(() -> new ResponseEntity<>(null,BAD_REQUEST));
    }

    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteComment(@RequestBody CommentForum commentForum) {
        return commentForumRepository.findById(commentForum.getIdComment())
                .map( (value) -> {
                    commentForumRepository.deleteById(commentForum.getIdComment());
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( () -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }
}
