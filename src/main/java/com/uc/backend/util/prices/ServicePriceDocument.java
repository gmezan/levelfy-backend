package com.uc.backend.util.prices;

import com.uc.backend.enums.LevelfyServiceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

public abstract class ServicePriceDocument {

    public enum LevelfyGeneralServiceType {
        EXAM, PC, ANY
    }

    protected BigDecimal price;
    protected Integer hours;
    @Enumerated(EnumType.STRING)
    protected LevelfyGeneralServiceType type;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public LevelfyGeneralServiceType getType() {
        return type;
    }

    public void setType(LevelfyGeneralServiceType type) {
        this.type = type;
    }
}
