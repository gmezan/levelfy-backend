package com.uc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "video")
public class Video extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvideo")
    private int idVideo;

    @ManyToOne
    @JoinColumn(name = "idclase", nullable = false)
    private Service service;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, name = "nombre")
    private String name;

    @Column(name = "evaluacion")
    private Integer evaluation;

    @Column(nullable = false, name = "numero_orden")
    private int order;

    @Column(nullable = true, name = "description")
    private String description;


    public int getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(int idvideo) {
        this.idVideo = idvideo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Service getClase() {
        return service;
    }

    public void setClase(Service service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripcion) {
        this.description = descripcion;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int orden) {
        this.order = orden;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    /*
    public String getEvaluacion() {
        return EVALUACION.get(evaluacion);
    }*/

    public void setEvaluation(Integer evaluacion) {
        this.evaluation = evaluacion;
    }
}
