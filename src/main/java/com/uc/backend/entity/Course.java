package com.uc.backend.entity;


import com.uc.backend.dto.CourseId;
import com.uc.backend.enums.UniversityName;

import javax.persistence.*;
import java.io.Serializable;

import static com.uc.backend.util.CustomConstants.UNIVERSITIES;

@Entity
@Table(name =  "curso")
public class Course extends Auditable implements Serializable {

    @EmbeddedId
    private CourseId courseId;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "ciclo")
    private Integer cycle;

    @Column(name = "foto_url")
    private String photo;

    @Column(name = "descripcion", nullable = true)
    private String description;

    public Course(){}

    public Course(CourseId courseId){
        this.courseId = courseId;
    }

    public Course(UniversityName university){
        this.courseId = new CourseId();
        this.courseId.setUniversity(university);
    }

    public String idToString(){
        return this.courseId.getIdCourse() + "_" + this.courseId.getUniversity();
    }


    public String getUnivNameStr(){
        return UNIVERSITIES.get(courseId.getUniversity());
    }

    public CourseId getCourseId() {
        return courseId;
    }

    public void setCourseId(CourseId courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
