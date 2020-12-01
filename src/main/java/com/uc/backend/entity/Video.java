package com.uc.backend.entity;

import com.uc.backend.config.Auditable;

import javax.persistence.*;
import java.io.Serializable;

import static com.uc.backend.utils.CustomConstants.EVALUACION;

@Entity
@Table(name = "video")
public class Video extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvideo")
    private int idvideo;

    @ManyToOne
    @JoinColumn(name = "idclase", nullable = false)
    private Clase clase;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, name = "nombre")
    private String nombre;

    @Column(name = "evaluacion")
    private Integer evaluacion;

    @Column(nullable = false, name = "numero_orden")
    private int orden;

    @Column(nullable = true)
    private String descripcion;


    public int getIdvideo() {
        return idvideo;
    }

    public void setIdvideo(int idvideo) {
        this.idvideo = idvideo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    /*
    public String getEvaluacion() {
        return EVALUACION.get(evaluacion);
    }*/

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }
}
