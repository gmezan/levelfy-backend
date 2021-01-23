package com.uc.backend.model;


import javax.persistence.*;
import java.io.Serializable;

import static com.uc.backend.util.CustomConstants.ROLES;

@Entity
@Table(name = "rol")
public class Role implements Serializable {

    @Id
    @Column(name = "idrol")
    private int idRole; // student's role

    @Column(name = "nombre")
    private String name;

    public Role(){}

    public Role(int r){
        idRole = r;
        name = ROLES.get(r);
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
