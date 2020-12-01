package com.uc.backend.entity;


import com.uc.backend.config.Auditable;
import com.uc.backend.dto.RegistroPagoDto;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
public class Venta extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idventa;

    @ManyToOne
    @JoinColumn(name = "idclase_enroll")
    private ClaseEnroll claseEnroll;

    @Column(nullable = false, name = "fecha_pago")
    private LocalDateTime fechapago;

    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechavencimiento;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "usuario")
    private String persona;

    @Column(name="cupon")
    private String cupon;

    @Column
    private String mensaje;

    @Column
    private String metodo;

    public Venta(){}

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
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

    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }

    public ClaseEnroll getClaseEnroll() {
        return claseEnroll;
    }

    public void setClaseEnroll(ClaseEnroll claseEnroll) {
        this.claseEnroll = claseEnroll;
    }

    public LocalDateTime getFechapago() {
        return fechapago;
    }

    public LocalDateTime getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(LocalDateTime fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public void setFechapago(LocalDateTime fechapago) {
        this.fechapago = fechapago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getCupon() {
        return cupon;
    }

    public void setCupon(String cupon) {
        this.cupon = cupon;
    }
}
