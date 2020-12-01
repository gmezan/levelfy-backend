package com.uc.backend.entity;

import com.uc.backend.config.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

import static com.uc.backend.utils.CustomConstants.DIA;

@Entity
@Table(name = "disponibilidad_profesor")
@JsonIgnoreProperties("usuario")
public class DisponibilidadProfesor extends Auditable implements Serializable, Comparable<DisponibilidadProfesor> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddisponibilidad")
    private int id;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private Usuario usuario;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "inicio",nullable = false)
    private LocalTime inicio;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "fin",nullable = false)
    private LocalTime fin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dia", nullable = false)
    private Integer dia;

    @Column(nullable = false)
    private boolean ocupado = false;

    @Override
    public int compareTo(DisponibilidadProfesor o) {
        return this.getDia().compareTo(o.getDia());
    }


    public ArrayList<LocalTime> splitTimes(){
        return new ArrayList<LocalTime>(){{
            for (int i = 0; i < (fin.getHour() - inicio.getHour()); i++){
                add(inicio.plusHours(i));
            }
        }};
    }

    public String getDayName(){
        return DIA.get(dia);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }


}
