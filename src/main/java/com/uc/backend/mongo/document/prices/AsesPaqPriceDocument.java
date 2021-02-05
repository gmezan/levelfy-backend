package com.uc.backend.mongo.document.prices;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.math.BigDecimal;

@Document(collection = "price-asespaq")
public class AsesPaqPriceDocument {

    public enum AsesPaqType {
        EXAM, PC
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String university;
    @Enumerated(EnumType.STRING)
    private AsesPaqType type;
    private int hours;
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AsesPaqType getType() {
        return type;
    }

    public void setType(AsesPaqType type) {
        this.type = type;
    }
}
