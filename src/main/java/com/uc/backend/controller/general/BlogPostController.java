package com.uc.backend.controller.general;

import com.uc.backend.entity.BlogPost;
import com.uc.backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
@CrossOrigin
@RestController
@RequestMapping("/model/blog")
public class BlogPostController {

    BlogRepository blogRepository;

    @Autowired
    public BlogPostController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }


    // RESTFUL

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogPost>> getBlogList() {
        return new ResponseEntity<>(blogRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{i}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogPost> getBlogElement(@PathVariable("i") int idBlog) {
        return blogRepository.findById(idBlog).map((value)-> new ResponseEntity<>(value,OK))
                .orElseGet(() -> new ResponseEntity<>(null,BAD_REQUEST));
    }

}
