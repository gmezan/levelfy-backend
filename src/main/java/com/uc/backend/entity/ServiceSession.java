package com.uc.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clase_sesion")
public class ServiceSession extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsesion")
    private int idServiceSession;

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


    public ServiceSession(){

    }
    public ServiceSession(Service service){
        this.service = service;
    }

    public ServiceSession(Service service, ServiceSession serviceSession){
        this.service = service;
        this.date = serviceSession.date;
        this.start = serviceSession.start;
        this.end = serviceSession.end;
    }

    public int getIdServiceSession() {
        return idServiceSession;
    }

    public void setIdServiceSession(int idEnrollmentSession) {
        this.idServiceSession = idEnrollmentSession;
    }

    public Service getService() { return service; }

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

}
