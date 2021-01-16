package com.uc.backend.controller.general;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.repository.CommentForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("comment-forum")
public class CommentForumController {
    @Autowired
    CommentForumRepository commentForumRepository;

    @GetMapping(value = "/dev/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentForum>> getAll(){
        return new ResponseEntity<>(commentForumRepository.findAll(), HttpStatus.OK);
    }

}
