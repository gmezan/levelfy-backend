package com.uc.backend.entity;


import com.uc.backend.config.Auditable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sugerencia_curso")
public class CourseSuggestion extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsugerencia_curso")
    private int idSuggestion;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = true)
    private User user;

    @Column(nullable = false, name="nombre")
    private String name;

    @Column(nullable = false, name="servicio")
    private String service;


    public int getIdSuggestion() {
        return idSuggestion;
    }

    public void setIdSuggestion(int idSuggestion) {
        this.idSuggestion = idSuggestion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
