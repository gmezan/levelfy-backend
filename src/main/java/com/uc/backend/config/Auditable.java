package com.uc.backend.config;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP",name = "creation", nullable = false, updatable= false)
    protected LocalDateTime fechacreacion;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP",name = "modified", nullable = false)
    protected LocalDateTime fechamodificacion;


    public LocalDateTime getFechacreacion() {
        return fechacreacion;
    }
    public String obtenerFechacreacionStr() {
        return fechacreacion.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDateTime getFechamodificacion() {
        return fechamodificacion;
    }
    public String obtenerFechamodificacionStr() {
        return fechamodificacion.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}