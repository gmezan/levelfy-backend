package com.uc.backend.entity;

import com.google.api.client.util.DateTime;

import javax.persistence.*;
import java.time.*;


@Entity
@Table(name="blog_post")
public class Blog {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="idblogPost",nullable = false)
        private int idblogPost;
        @Column(name="title",nullable = false)
        private String title;
        @Column(name="thumbnail",nullable = false)
        private String thumbnail;
        @Column(name="author",nullable = false)
        private String author;
        @Column(name="photoAuthor",nullable = false)
        private String photoAuthor;
        @Column(name="body",nullable = false)
        private String body;
        @Column(name="dateTime",nullable = false)
        private DateTime dateTime;
        @Column(name="timeRead",nullable = false)
        private LocalTime timeRead;


        public int getIdblogPost() { return idblogPost; }
        public void setIdblogPost(int value) { this.idblogPost = value; }

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


        public DateTime getDateTime() {
                return dateTime;
        }

        public void setDateTime(DateTime dateTime) {
                this.dateTime = dateTime;
        }

        public LocalTime getTimeRead() {
                return timeRead;
        }

        public void setTimeRead(LocalTime timeRead) {
                this.timeRead = timeRead;
        }
}


