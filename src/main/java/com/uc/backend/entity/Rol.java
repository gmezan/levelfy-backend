package com.uc.backend.entity;


import javax.persistence.*;
import java.io.Serializable;

import static com.uc.backend.utils.CustomConstants.ROLS;

@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    @Id
    @Column(name = "idrol")
    private int idrol; // rol de estudiante

    @Column(name = "nombre")
    private String nombre;

    public Rol(){};

    public Rol(int r){
        idrol = r;
        nombre = ROLS.get(r);
    };

    public int getIdrol() {
        return idrol;
    }

    public void setIdrol(int idrol) {
        this.idrol = idrol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
