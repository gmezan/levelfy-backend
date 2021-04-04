package com.uc.backend.entity;


import com.sun.istack.NotNull;
import com.uc.backend.enums.RoleName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


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

    public Role(int idRole, RoleName name) {
        this.idRole = idRole;
        this.name = name;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return idRole == role.idRole &&
                name == role.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRole, name);
    }
}
