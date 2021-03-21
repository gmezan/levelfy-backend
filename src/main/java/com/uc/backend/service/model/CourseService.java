package com.uc.backend.service.model;

import com.uc.backend.entity.Course;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class CourseService {

    CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCoursesOfAvailableServicesByServiceType(LevelfyServiceType levelfyServiceType) {
        return courseRepository.findCoursesAvailableByServiceTypeAndAvailableIsTrue(levelfyServiceType.toString());
    }

}
