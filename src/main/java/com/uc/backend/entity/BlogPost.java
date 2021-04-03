package com.uc.backend.entity;

import com.google.api.client.util.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.*;


@Entity
@Table(name="blog_post")
public class BlogPost extends Auditable implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="idblog_post",nullable = false)
        private int id;

        @Column(name="title",nullable = false)
        private String title;
        @Column(name="thumbnail",nullable = false)
        private String thumbnail;
        @Column(name="description",nullable = false)
        private String description;
        @Column(name="author",nullable = false)
        private String author;
        @Column(name="photo_author",nullable = false)
        private String photoAuthor;
        @Column(name="badge",nullable = true)
        private String badge;
        @Column(name="date_time",nullable = false)
        private LocalDateTime dateTime;
        @Column(name="time_read",nullable = false)
        private Integer timeRead;
        @Column(name="fragments",nullable = false)
        private String fragments;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getFragments() {
        return fragments;
    }

    public void setFragments(String fragments) {
        this.fragments = fragments;
    }

    public int getId() { return id; }
    public void setId(int value) { this.id = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String value) { this.thumbnail = value; }

    public String getAuthor() { return author; }
    public void setAuthor(String value) { this.author = value; }

    public String getPhotoAuthor() { return photoAuthor; }
    public void setPhotoAuthor(String value) { this.photoAuthor = value; }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getTimeRead() {
        return timeRead;
    }

    public void setTimeRead(Integer timeRead) {
        this.timeRead = timeRead;
    }
}


