package com.uc.backend.entity;

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
    @Column(columnDefinition = "TIMESTAMP",name = "created", nullable = false, updatable = false)
    protected LocalDateTime created;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP",name = "modified", nullable = false)
    protected LocalDateTime modified;


    public LocalDateTime getCreated() {
        return created;
    }

    public String printCreatedToString() {
        return created.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public String printModifiedToString() {
        return modified.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}