package com.uc.backend.controller.roles.client;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.service.model.ForumService;
import com.uc.backend.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/c/forum")
public class ClientForumController {

    ForumService forumService;
    UserService userService;

    @Autowired
    public ClientForumController(ForumService forumService) {
        this.forumService = forumService;
    }


    @GetMapping(value = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentForum>> getAll() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> getCommentForumById(
            @PathVariable("id") int id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping(value = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentForum> postCommmentForum(
            @RequestBody CommentForum commentForum) {
        return new ResponseEntity<>(forumService.create(commentForum), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteForum(
            @PathVariable("id") int id) {
        forumService.getById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
