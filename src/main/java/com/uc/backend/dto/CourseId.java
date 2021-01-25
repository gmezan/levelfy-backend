package com.uc.backend.dto;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;


@Embeddable
public class CourseId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcurso")
    private String idCourse;

    @Column(nullable = false, name = "universidad")
    private String university;

    public CourseId(){}

    public CourseId(String idCourse, String university){
        this.idCourse = idCourse;
        this.university = university;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof CourseId)
        {
            CourseId courseId = (CourseId)obj;
            return (courseId.idCourse.equals(this.idCourse)) && (courseId.university.equals(this.university));
        }
        return false;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
