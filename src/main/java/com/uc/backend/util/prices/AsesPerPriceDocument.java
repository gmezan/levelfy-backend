package com.uc.backend.util.prices;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AsesPerPriceDocument {

    private String id;
    private String university;
    private ArrayList<BigDecimal> clientsPrice;

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

    public ArrayList<BigDecimal> getClientsPrice() {
        return clientsPrice;
    }

    public void setClientsPrice(ArrayList<BigDecimal> clientsPrice) {
        this.clientsPrice = clientsPrice;
    }
}
