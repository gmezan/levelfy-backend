package com.uc.backend.entity;


import com.sun.istack.NotNull;
import com.uc.backend.enums.RoleName;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "rol")
public class Role implements Serializable {

    @Id
    @Column(name = "idrol")
    private int idRole; // student's role

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "nombre", unique = true)
    private RoleName name;

    public Role(){}

    public Role(int r){
        idRole = r;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
