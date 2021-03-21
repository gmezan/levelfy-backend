package com.uc.backend.dto;

import com.uc.backend.enums.UniversityName;

import java.util.List;
import java.util.Map;


public class CourseInfoDto {

    protected String courseName;
    protected String courseId;
    protected UniversityName university;

    public CourseInfoDto(ServiceTeachDto serviceTeachDto) {
        this.courseId = serviceTeachDto.getCourseId();
        this.courseName = serviceTeachDto.getCourseName();
        this.university = serviceTeachDto.getUniversity();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public UniversityName getUniversity() {
        return university;
    }

    public void setUniversity(UniversityName university) {
        this.university = university;
    }
}
