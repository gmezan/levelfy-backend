package com.uc.backend.entity;


import com.uc.backend.config.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import static com.uc.backend.utils.CustomConstants.EVALUACION;

@Entity
@Table(name = "clase")
public class Clase extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclase")
    private int idclase;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "idcurso", nullable = false, referencedColumnName = "idcurso"),
            @JoinColumn(name = "universidad", nullable = false, referencedColumnName = "universidad")
    })
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "idprofesor", nullable = false)
    private Usuario profesor;

    @Column(name = "foto_url", nullable = false)
    private String foto;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(nullable = false)
    private String servicio;

    @Column(name = "precio_individual", nullable = false)
    private BigDecimal precio;

    @Column
    private Integer evaluacion;

    @Column
    private String descripcion;

    @Column
    private String paquete;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_vencimiento")
    private LocalDate vencimiento;

    @Column(name = "num_sesiones")
    private Integer numSesiones;

    @Column(name = "archived")
    private Boolean archived = false;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "clase")
    private List<ClaseEnroll> claseEnrollList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clase")
    private List<ClaseSesion> claseSesions;

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Integer getNumSesiones() {
        return numSesiones;
    }

    public void setNumSesiones(Integer numSesiones) {
        this.numSesiones = numSesiones;
    }

    public void addClaseSesion(ClaseSesion claseSesion){
        this.claseSesions.add(claseSesion);
    }

    public String getCreationDateGMTp5(){
        return super.fechacreacion.minusHours(5).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

    public String getVencimientoStr(){
        return vencimiento.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public List<ClaseEnroll> getClaseEnrollActiveList() {
        return new ArrayList<ClaseEnroll>(){{
            claseEnrollList.forEach(claseEnroll -> {if(claseEnroll.getActive()) add(claseEnroll);});
        }};
    }

    public List<ClaseEnroll> getClaseEnrollNonActiveList() {
        return new ArrayList<ClaseEnroll>(){{
            claseEnrollList.forEach(claseEnroll -> {if(!claseEnroll.getActive()) add(claseEnroll);});
        }};
    }

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }

    public List<ClaseEnroll> getClaseEnrollList() {
        return claseEnrollList;
    }

    public void setClaseEnrollList(List<ClaseEnroll> claseEnrollList) {
        this.claseEnrollList = claseEnrollList;
    }

    public List<ClaseSesion> getClaseSesions() {
        return claseSesions;
    }

    public void setClaseSesions(List<ClaseSesion> claseSesions) {
        this.claseSesions = claseSesions;
    }

    public int getIdclase() {
        return idclase;
    }

    public void setIdclase(int idclase) {
        this.idclase = idclase;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    public String getNombreEvaluacion(){
        return EVALUACION.get(this.evaluacion);
    }

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }
}
