package com.uc.backend.service.model;

import com.uc.backend.dto.PaymentDto;
import com.uc.backend.entity.Sale;
import com.uc.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleService {
    SaleRepository saleRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }

    public Sale registerClientPayment(PaymentDto paymentDto) {
        return new Sale();
    }
}
