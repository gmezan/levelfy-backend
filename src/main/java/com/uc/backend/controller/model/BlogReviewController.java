package com.uc.backend.controller.model;

import com.uc.backend.entity.BlogPost;
import com.uc.backend.entity.BlogReview;
import com.uc.backend.repository.BlogReviewRepository;
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
@RequestMapping("/model/blog-review")
public class BlogReviewController {

    BlogReviewRepository blogReviewRepository;

    @Autowired
    public BlogReviewController(BlogReviewRepository blogReviewRepository){
        this.blogReviewRepository = blogReviewRepository;
    }

    // RESTFUL

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogReview>> getBlogReviewList() {
        return new ResponseEntity<>(blogReviewRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{i}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogReview> getBlogReviewElement(@PathVariable("i") int idBlogReview) {
        return blogReviewRepository.findById(idBlogReview).map((value)-> new ResponseEntity<>(value,OK))
                .orElseGet(() -> new ResponseEntity<>(null,BAD_REQUEST));
    }

/*    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogPost> insertBlogPost(@RequestBody BlogPost blogPost) {
        return blogReviewRepository.findById(blogPost.getId())
                .map((value) -> new ResponseEntity<>(value, BAD_REQUEST))
                .orElseGet(() -> new ResponseEntity<>(blogRepository.save(blogPost), OK));
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogPost> updateBlogPost(@RequestBody BlogPost blogPost) {
        return blogReviewRepository.findById(blogPost.getId())
                .map((value) -> new ResponseEntity<>(blogRepository.save(blogPost), OK))
                .orElseGet(() -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @DeleteMapping(value = "{i}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBlogPost(@PathVariable int idBlogPost) {
        return blogReviewRepository.findById(idBlogPost)
                .map((value) -> {
                    blogRepository.deleteById(idBlogPost);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet(() -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }*/
}
