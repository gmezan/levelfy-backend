package com.uc.backend.entity;


import com.uc.backend.config.Auditable;
import com.uc.backend.dto.CursoId;

import javax.persistence.*;
import java.io.Serializable;

import static com.uc.backend.utils.CustomConstants.UNIVERSIDAD;

@Entity
@Table(name =  "curso")
public class Curso extends Auditable implements Serializable {

    @EmbeddedId
    private CursoId cursoId;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "ciclo")
    private Integer ciclo;

    @Column(name = "foto_url")
    private String foto;

    @Column(nullable = false)
    private String descripcion;

    public Curso(){}

    public Curso(CursoId cursoId){
        this.cursoId = cursoId;
    }

    public Curso(String universidad){
        this.cursoId = new CursoId();
        this.cursoId.setUniversidad(universidad);
    }


    public String getUnivNameStr(){
        return UNIVERSIDAD.get(cursoId.getUniversidad());
    }


    public CursoId getCursoId() {
        return cursoId;
    }

    public void setCursoId(CursoId cursoId) {
        this.cursoId = cursoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
