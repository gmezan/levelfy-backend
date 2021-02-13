package com.uc.backend.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.uc.backend.enums.LevelfyServiceType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clase")
public class Service extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclase")
    private int idService;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "idcurso", nullable = false, referencedColumnName = "idcurso"),
            @JoinColumn(name = "universidad", nullable = false, referencedColumnName = "universidad")
    })
    private Course course;


    @ManyToOne
    @JoinColumn(name = "idprofesor", nullable = false)
    private User teacher;

    @Column(name = "foto_url", nullable = false)
    private String photo;

    @Column(nullable = false, name = "disponible")
    private Boolean available;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio" ,nullable = false)
    private LevelfyServiceType serviceType;

    @Column(name = "precio_individual", nullable = false)
    private BigDecimal price;

    @Column(name = "evaluacion")
    private Integer evaluation;

    @Column(name = "descripcion")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_vencimiento")
    private LocalDate expiration;

    @Column(name = "archived")
    private Boolean archived = false;


    // OneToMany Relations:

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "service")
    private List<Enrollment> enrollmentList;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    private List<EnrollmentSession> enrollmentSessionList;


    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public LevelfyServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(LevelfyServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public List<Enrollment> getEnrollmentList() {
        return enrollmentList;
    }

    public void setEnrollmentList(List<Enrollment> enrollmentList) {
        this.enrollmentList = enrollmentList;
    }

    public List<EnrollmentSession> getEnrollmentSessionList() {
        return enrollmentSessionList;
    }

    public void setEnrollmentSessionList(List<EnrollmentSession> enrollmentSessions) {
        this.enrollmentSessionList = enrollmentSessions;
    }
}
