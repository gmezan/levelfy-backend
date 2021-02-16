package com.uc.backend.service.prices;

import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.util.prices.ServicePriceDocument;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static com.uc.backend.util.CustomConstants.PRICES;

@Component
public class LevelfyServicePriceRepository {

    public HashMap<UniversityName, HashMap<LevelfyServiceType, List<ServicePriceDocument>>> getAllPrices() {
        return PRICES;
    }

}
