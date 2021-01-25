package com.uc.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clase_sesion")
public class EnrollmentSession extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsesion")
    private int idEnrollmentSession;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idclase")
    private Service service;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(name = "inicio", nullable = false)
    private LocalTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(name = "fin", nullable = false)
    private LocalTime end;

    @Column(name = "sesion_url")
    private String zoom;

    @Column(name = "descripcion")
    private String description;


    public EnrollmentSession(){

    }
    public EnrollmentSession(Service service){
        this.service = service;
    }

    public EnrollmentSession(Service service, EnrollmentSession enrollmentSession){
        this.service = service;
        this.date = enrollmentSession.date;
        this.start = enrollmentSession.start;
        this.end = enrollmentSession.end;
        this.zoom= enrollmentSession.zoom;
    }

    public int getIdEnrollmentSession() {
        return idEnrollmentSession;
    }

    public void setIdEnrollmentSession(int idEnrollmentSession) {
        this.idEnrollmentSession = idEnrollmentSession;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
