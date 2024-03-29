package com.uc.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clase_enroll")
public class Enrollment extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclase_enroll")
    private int idEnrollment;

    @JsonIgnoreProperties(value = {"enrollmentList", "serviceSessionList", "serviceAgendaList"})
    @ManyToOne
    @JoinColumn(name = "idclase", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "idalumno", nullable = false)
    private User student;

    @Column(name = "pago_confirmado",nullable = false)
    private Boolean payed;

    @Column(name = "activo", nullable = false)
    private Boolean active;

    //For "ASES-PER"

    @Column(name = "cantidad_persona")
    private Integer numberOfStudents;

    @Column(name = "ases_per_inicio")
    private LocalDateTime start;

    @Column(name = "ases_per_fin")
    private LocalDateTime end;

    @Column(name = "url_sesion")
    private String url;

    @Column(name = "precio")
    private BigDecimal price;

    @Column(name = "expiration_datetime")
    private LocalDateTime expiration;


    @JsonIgnore
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Sale> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<Sale> saleList) {
        this.saleList = saleList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
