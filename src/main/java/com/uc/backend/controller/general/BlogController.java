package com.uc.backend.controller.general;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Blog;
import com.uc.backend.entity.Service;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.BlogRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.ServiceSessionRepository;
import com.uc.backend.service.general.ServiceService;
import com.uc.backend.service.general.UserService;
import com.uc.backend.service.prices.LevelfyServicePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
@CrossOrigin
@RestController
@RequestMapping("/blog")
public class BlogController {
@Autowired
BlogRepository blogRepository;

    @Autowired
    public BlogController(BlogRepository blogRepository) {
    this.blogRepository=blogRepository;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Blog> getBlogElement(
            @RequestParam(value = "id") int idBlog
    ) {

    return blogRepository.findById(idBlog).map((value)-> new ResponseEntity<>(value,OK)).orElseGet(() -> new ResponseEntity<>(null,BAD_REQUEST));



    }
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List
    <Blog>> getBlogList()
    {    return new ResponseEntity<>(blogRepository.findAll(),HttpStatus.OK);}



    // Web Service for forms


}
