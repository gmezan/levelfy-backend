package com.uc.backend.dto;

import com.uc.backend.enums.UniversityName;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CourseInfoDto {

    protected String courseName;
    protected String courseId;
    protected UniversityName university;

    public CourseInfoDto(ServiceTeachDto serviceTeachDto) {
        this.courseId = serviceTeachDto.getCourseId();
        this.courseName = serviceTeachDto.getCourseName();
        this.university = serviceTeachDto.getUniversity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseInfoDto that = (CourseInfoDto) o;
        return Objects.equals(courseName, that.courseName) &&
                Objects.equals(courseId, that.courseId) &&
                university == that.university;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, courseId, university);
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
