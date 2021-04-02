package com.uc.backend.service.model;

import com.uc.backend.dto.PaymentDto;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.Sale;
import com.uc.backend.entity.User;
import com.uc.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleService {
    SaleRepository saleRepository;
    EnrollmentService enrollmentService;

    @Autowired
    public SaleService(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }

    public Sale registerClientPayment(PaymentDto paymentDto, User user, Enrollment enrollment) {

        Sale sale = new Sale();
        sale.setEnrollment(enrollment);
        sale.setSaleDateTime(paymentDto.getDate());
        sale.setAmount(paymentDto.getAmount());
        sale.setPersona(paymentDto.getPersona());
        sale.setMessage(paymentDto.getMessage());
        sale.setMethod(paymentDto.getMethod());

        return sale;
    }
}
