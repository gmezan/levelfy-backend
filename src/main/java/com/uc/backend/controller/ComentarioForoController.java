package com.uc.backend.controller;

import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/c")
public class ComentarioForoController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    EnrollmentSessionRepository enrollmentSessionRepository;
}
