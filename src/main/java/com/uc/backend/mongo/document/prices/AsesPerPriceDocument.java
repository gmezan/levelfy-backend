package com.uc.backend.mongo.document.prices;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;

@Document(collection = "price-asesper")
public class AsesPerPriceDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
