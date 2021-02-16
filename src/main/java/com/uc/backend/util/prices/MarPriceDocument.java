package com.uc.backend.util.prices;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GenerationType;
import java.math.BigDecimal;

public class MarPriceDocument {

    private String id;
    private String university;
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
}
