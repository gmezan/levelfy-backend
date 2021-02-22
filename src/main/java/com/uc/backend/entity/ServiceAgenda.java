package com.uc.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "clase_agenda")
public class ServiceAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclase_temario")
    private Integer id;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "description", nullable = false)
    private String description;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "clase_idclase")
    private Service service;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
