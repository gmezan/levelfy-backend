package com.uc.backend.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.uc.backend.utils.CustomConstants.METODOS_DE_PAGO;

public class RegistroPagoDto {
    private String persona;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fecha;
    private String correo;
    private String mensaje;
    private BigDecimal monto;
    private Integer metodo;
    private Integer idclase;
    private String cupon;


    public String getMetodoStr(){
        return METODOS_DE_PAGO[metodo];
    }

    public Integer getIdclase() {
        return idclase;
    }

    public void setIdclase(Integer idclase) {
        this.idclase = idclase;
    }

    public boolean validarRegistro(){
        boolean ver = true;
        ver = fecha != null && persona != null;
        ver = ver && (monto!=null) && (monto.compareTo(BigDecimal.ZERO)>0) && (monto.intValue()<9999999)
                && (monto.subtract(new BigDecimal(monto.intValue())).toPlainString().length()<=4);
        ver = ver && metodo!=null && (metodo>=0 && metodo<=4) && (idclase!=null);
        return ver;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona.trim();
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo.trim();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje.trim();
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getMetodo() {
        return metodo;
    }

    public void setMetodo(Integer metodo) {
        this.metodo = metodo;
    }

    public String getCupon() {
        return cupon;
    }

    public void setCupon(String cupon) {
        this.cupon = cupon;
    }
}
