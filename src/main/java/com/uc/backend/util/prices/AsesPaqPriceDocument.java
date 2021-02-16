package com.uc.backend.util.prices;


import javax.persistence.*;
import java.math.BigDecimal;

public class AsesPaqPriceDocument extends ServicePriceDocument {

    public AsesPaqPriceDocument(BigDecimal price, Integer hours, LevelfyGeneralServiceType levelfyGeneralServiceType) {
        this.price = price;
        this.hours = hours;
        this.type = levelfyGeneralServiceType;
    }

}
