package com.uc.backend.util.prices;


import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AsesPerPriceDocument extends ServicePriceDocument {

    protected Integer clients;

    public AsesPerPriceDocument(BigDecimal price, Integer clients) {
        this.price = price;
        this.clients = clients;
        this.hours = 1;
        this.type = ServicePriceDocument.LevelfyGeneralServiceType.ANY;
    }

}
