package com.uc.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clase_enroll")
public class Enrollment extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclase_enroll")
    private int idEnrollment;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idclase", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "idalumno", nullable = false)
    private User student;

    @Column(name = "pago_confirmado",nullable = false)
    private Boolean payed;

    //For "ASES-PER"

    @Column(name = "cantidad_persona")
    private Integer numberOfStudents;

    @Column(name = "ases_per_inicio")
    private LocalDateTime start;

    @Column(name = "ases_per_fin")
    private LocalDateTime end;

    @Column(name = "info")
    private String info;

    @Column(name = "activo")
    private Boolean active;

    @Column(name = "conference_url")
    private String url;



    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "enrollment")
    private List<Sale> saleList;


    public int getIdEnrollment() {
        return idEnrollment;
    }

    public void setIdEnrollment(int idEnrollment) {
        this.idEnrollment = idEnrollment;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Sale> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<Sale> saleList) {
        this.saleList = saleList;
    }
}
