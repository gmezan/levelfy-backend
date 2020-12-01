package com.uc.backend.entity;

import java.math.BigDecimal;

public class Paquete {
    private String idpaquete;
    //Para asesorías personalizadas y paquetes de asesorías
    private int numeroSesiones;
    private String servicio;
    private String descripcion;

    //Para clases selfPaced
    private int tiempoClase;
    private String tiempoClaseStr;

    public Paquete(){}

    //Para asesorias personalizadas y paquetes de asesorías
    public Paquete(String idpaquete,int numeroSesiones, String servicio){
        this.idpaquete=idpaquete;
        this.numeroSesiones=numeroSesiones;
        this.servicio=servicio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getIdpaquete() {
        return idpaquete;
    }

    public void setIdpaquete(String idpaquete) {
        this.idpaquete = idpaquete;
    }

    public int getNumeroSesiones() {
        return numeroSesiones;
    }

    public void setNumeroSesiones(int numeroSesiones) {
        this.numeroSesiones = numeroSesiones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
