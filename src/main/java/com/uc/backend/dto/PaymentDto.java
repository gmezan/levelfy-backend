package com.uc.backend.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.uc.backend.util.CustomConstants.METODOS_DE_PAGO;

public class PaymentDto {
    private String persona;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    private String email;
    private String message;
    private BigDecimal amount;
    private Integer method;
    private Integer idclase;
    private String coupon;


    public String getMethodStr(){
        return METODOS_DE_PAGO[method];
    }

    public Integer getIdclase() {
        return idclase;
    }

    public void setIdclase(Integer idclase) {
        this.idclase = idclase;
    }

    public boolean validatePayment() {
        boolean ver = true;
        ver = date != null && persona != null;
        ver = ver && (amount !=null) && (amount.compareTo(BigDecimal.ZERO)>0) && (amount.intValue()<9999999)
                && (amount.subtract(new BigDecimal(amount.intValue())).toPlainString().length()<=4);
        ver = ver && method !=null && (method >=0 && method <=4) && (idclase!=null);
        return ver;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona.trim();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}
