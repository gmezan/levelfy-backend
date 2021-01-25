package com.uc.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

@Entity
@Table(name = "disponibilidad_profesor")
@JsonIgnoreProperties("user")
public class TeacherAvailability extends Auditable implements Serializable, Comparable<TeacherAvailability> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddisponibilidad")
    private int id;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private User user;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "inicio",nullable = false)
    private LocalTime start;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "fin",nullable = false)
    private LocalTime end;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dia", nullable = false)
    private Integer day;

    @Column(nullable = false, name = "ocupado")
    private boolean busy = false;

    @Override
    public int compareTo(TeacherAvailability o) {
        return this.getDay().compareTo(o.getDay());
    }


    public ArrayList<LocalTime> splitTimes(){
        return new ArrayList<LocalTime>(){{
            for (int i = 0; i < (end.getHour() - start.getHour()); i++){
                add(start.plusHours(i));
            }
        }};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
