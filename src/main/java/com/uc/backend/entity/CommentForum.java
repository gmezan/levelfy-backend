package com.uc.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentario_foro")
public class CommentForum extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomentario_foro")
    private int idComment;

    @Column(nullable = false, name = "comentario")
    private  String comment;

    @Column(name = "foto_url")
    private String photo;

    @JsonIgnoreProperties(
            {"created","modified","role","coupon","phone","balance","birthday","token","invitingId",
                "active","code"})
    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private User user;

    @Column(nullable = false, name = "fecha")
    private LocalDateTime dateTime;

    @JsonIgnoreProperties(value = {"enrollmentList", "serviceSessionList", "serviceAgendaList"})
    /*@JsonIgnore*/
    @ManyToOne
    @JoinColumn(name = "idclase")
    private Service service;


    public CommentForum(){}

    public CommentForum(int id) {
        this.idComment = id;
    }


    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
