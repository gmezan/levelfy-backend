package com.uc.backend.dto;

import com.uc.backend.entity.Curso;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;


@Embeddable
public class CursoId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcurso")
    private String idcurso;

    @Column(nullable = false)
    private String universidad;


    public CursoId(){}

    public CursoId(String idcurso, String universidad){
        this.idcurso=idcurso;
        this.universidad=universidad;
    }

    public String getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(String idcurso) {
        this.idcurso = idcurso;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }
}
