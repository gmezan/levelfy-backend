package com.uc.backend.util.prices;
import java.math.BigDecimal;

public class MarPriceDocument extends ServicePriceDocument {

    public MarPriceDocument(BigDecimal price, Integer hours) {
        this.price = price;
        this.hours = hours;
        this.type = LevelfyGeneralServiceType.ANY;
    }

}