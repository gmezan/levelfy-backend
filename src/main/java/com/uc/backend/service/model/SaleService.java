package com.uc.backend.service.model;

import com.uc.backend.dto.PaymentDto;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.Sale;
import com.uc.backend.entity.User;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService {
    SaleRepository saleRepository;
    EnrollmentService enrollmentService;
    EnrollmentRepository enrollmentRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, EnrollmentService enrollmentService,
                       EnrollmentRepository enrollmentRepository){
        this.saleRepository = saleRepository;
        this.enrollmentService = enrollmentService;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Sale registerClientPayment(PaymentDto paymentDto, User user, Enrollment enrollment) {

        // TODO: Simple validation

        Sale sale = new Sale();
        sale.setEnrollment(enrollment);
        sale.setSaleDateTime(paymentDto.getDate());
        sale.setAmount(paymentDto.getAmount());
        sale.setPersona(paymentDto.getPersona());
        sale.setMessage(paymentDto.getMessage());
        sale.setMethod(paymentDto.getMethod());

        // Saved
        Sale savedSale = saleRepository.save(sale);

        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollment.getIdEnrollment());

        if (!optionalEnrollment.isPresent())
            return null;

        Enrollment sameEnrollment = optionalEnrollment.get();

        List<Sale> saleList = sameEnrollment.getSaleList();

        List<Sale> saleListToDelete = new ArrayList<>();
        saleList.forEach(sale1 -> {
            if (sale1.getIdSale()!=savedSale.getIdSale())
                saleListToDelete.add(sale1);
        });

        saleRepository.deleteAll(saleListToDelete);

        return savedSale;

    }
}
