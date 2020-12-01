package com.uc.backend.entity;

import com.uc.backend.config.Auditable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta_cancelada")
public class VentaCancelada extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idventa_cancelada")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idestudiante", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "idprofesor", nullable = false)
    private Usuario profesor;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "idcurso", nullable = false, referencedColumnName = "idcurso"),
            @JoinColumn(name = "universidad", nullable = false, referencedColumnName = "universidad")
    })
    private Curso curso;

    @Column(nullable = false)
    private String servicio;

    @Column(nullable = false, name = "fecha_pago")
    private LocalDateTime fechaDePago;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column
    private String mensaje;

    @Column
    private String metodo;

    @Column(nullable = false, name = "venta_creation")
    private LocalDateTime dateTimeVentaCreation;

    @Column
    private Boolean resuelto;


    public VentaCancelada(){}

    public VentaCancelada(Venta venta){
        this.estudiante = venta.getClaseEnroll().getEstudiante();
        this.profesor = venta.getClaseEnroll().getClase().getProfesor();
        this.curso = venta.getClaseEnroll().getClase().getCurso();
        this.servicio = venta.getClaseEnroll().getClase().getServicio();
        this.fechaDePago = venta.getFechapago();
        this.monto = venta.getMonto();
        this.mensaje = venta.getMensaje();
        this.metodo = venta.getMetodo();
        this.resuelto = Boolean.FALSE;
        this.dateTimeVentaCreation = venta.getFechacreacion();
    }

    public static List<VentaCancelada> generateVentaCanceladas(List<Venta> ventas){
        return new ArrayList<VentaCancelada>(){{
            ventas.forEach(venta -> add(new VentaCancelada(venta)));
        }};
    }


    public Boolean getResuelto() {
        return resuelto;
    }

    public void setResuelto(Boolean resuelto) {
        this.resuelto = resuelto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public LocalDateTime getFechaDePago() {
        return fechaDePago;
    }

    public void setFechaDePago(LocalDateTime fechaDePago) {
        this.fechaDePago = fechaDePago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public LocalDateTime getDateTimeVentaCreation() {
        return dateTimeVentaCreation;
    }

    public void setDateTimeVentaCreation(LocalDateTime dateTimeVentaCreation) {
        this.dateTimeVentaCreation = dateTimeVentaCreation;
    }
}
