package com.uc.backend.dto;

import com.sun.istack.NotNull;
import com.uc.backend.enums.UniversityName;

import javax.persistence.*;
import java.io.Serializable;


@Embeddable
public class CourseId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcurso")
    private String idCourse;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, name = "universidad")
    private UniversityName university;

    public CourseId(){}

    public CourseId(String idCourse, UniversityName university){
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

    public UniversityName getUniversity() {
        return university;
    }

    public void setUniversity(UniversityName university) {
        this.university = university;
    }
}
