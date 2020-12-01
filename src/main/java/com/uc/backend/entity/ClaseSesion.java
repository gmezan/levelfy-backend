package com.uc.backend.entity;

import com.uc.backend.config.Auditable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Entity
@Table(name = "clase_sesion")
public class ClaseSesion extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsesion")
    private int idClaseSesion;

    @ManyToOne
    @JoinColumn(name = "idclase")
    private Clase clase;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate fecha;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(nullable = false)
    private LocalTime inicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(nullable = false)
    private LocalTime fin;

    @Column(name = "sesion_url")
    private String zoom;

    @Column
    private String descripcion;


    public ClaseSesion(){

    }
    public ClaseSesion(Clase clase){
        this.clase = clase;
    }

    public ClaseSesion(Clase clase, ClaseSesion claseSesion){
        this.clase = clase;
        this.fecha = claseSesion.fecha;
        this.inicio = claseSesion.inicio;
        this.fin = claseSesion.fin;
        this.zoom= claseSesion.zoom;
    }

    public String getFechaStr(){
        return this.fecha.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }


    public String getHoraFin(int d){
        return fin.toString();
    }

    public int getIdClaseSesion() {
        return idClaseSesion;
    }

    public void setIdClaseSesion(int idClaseSesion) {
        this.idClaseSesion = idClaseSesion;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public LocalTime getFin() {
        return fin;
    }

    public void setFin(LocalTime fin) {
        this.fin = fin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
