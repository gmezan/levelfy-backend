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
        @Column(name="author",nullable = false)
        private String author;
        @Column(name="photo_author",nullable = false)
        private String photoAuthor;
        @Column(name="body",nullable = false)
        private String body;
        @Column(name="date_time",nullable = false)
        private LocalDateTime dateTime;
        @Column(name="time_read",nullable = false)
        private Integer timeRead;


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

        public String getBody() { return body; }
        public void setBody(String value) { this.body = value; }


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


