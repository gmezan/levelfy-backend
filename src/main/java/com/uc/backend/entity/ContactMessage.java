package com.uc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact_message")
public class ContactMessage extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcontact_message")
    private int id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "contact")
    private String contact;

    @Column(nullable = false, name = "message")
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
