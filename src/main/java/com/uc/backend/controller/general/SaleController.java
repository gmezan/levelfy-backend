package com.uc.backend.controller.general;

import com.uc.backend.entity.Sale;
import com.uc.backend.entity.SaleCanceled;
import com.uc.backend.entity.Service;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.SaleCanceledRepository;
import com.uc.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin
@RestController
@RequestMapping("model/sale")
public class SaleController {
    
    SaleRepository saleRepository;
    EnrollmentRepository enrollmentRepository;
    SaleCanceledRepository saleCanceledRepository;
    
    @Autowired
    public SaleController(SaleRepository saleRepository,EnrollmentRepository enrollmentRepository, SaleCanceledRepository saleCanceledRepository) {
        this.saleRepository = saleRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.saleCanceledRepository = saleCanceledRepository;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sale>> getAll(@RequestParam(name = "u", required = false) UniversityName u,
                                             @RequestParam(name = "s", required = false) LevelfyServiceType s,
                                             @RequestParam(name = "p", required = false) Boolean payed){

        List<Sale> saleList;

        if (u!=null && s!=null) saleList = saleRepository.findSalesByEnrollment_Service_Course_CourseId_UniversityAndEnrollment_Service_ServiceType(u, s);
        else if(u!=null) saleList = saleRepository.findSalesByEnrollment_Service_Course_CourseId_University(u);
        else if(s!=null) saleList = saleRepository.findSalesByEnrollment_Service_ServiceType(s);
        else saleList = saleRepository.findAll();

        if (payed == null) return new ResponseEntity<>(saleList, HttpStatus.OK);

        if (payed)
            return new ResponseEntity<>(saleList.stream().filter(serv->serv.getEnrollment().getPayed()).collect(Collectors.toList()), HttpStatus.OK);
        else
            return new ResponseEntity<>(saleList.stream().filter(serv->!serv.getEnrollment().getPayed()).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> getSale(@PathVariable("s") int idSale){
        return saleRepository.findById(idSale)
                .map( (sale) -> new ResponseEntity<>(sale,OK))
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> newSale(@RequestBody Sale sale) {
        return saleRepository.findById(sale.getIdSale())
                .map( (s) -> new ResponseEntity<>(s, BAD_REQUEST))
                .orElseGet( () ->
                        enrollmentRepository.findById(sale.getEnrollment().getIdEnrollment())
                                .map( (enrollment) -> {
                                    sale.setEnrollment(enrollment);
                                    return new ResponseEntity<>(saleRepository.save(sale),OK);
                                })
                                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST))
                );
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> updateSale(@RequestBody Sale sale) {
        return saleRepository.findById(sale.getIdSale())
                .map( (s) ->
                        enrollmentRepository.findById(sale.getEnrollment().getIdEnrollment())
                                .map( (enrollment) -> {
                                    sale.setEnrollment(enrollment);
                                    sale.getEnrollment().setPayed(Boolean.TRUE);
                                    enrollmentRepository.save(sale.getEnrollment());
                                    return new ResponseEntity<>(sale, OK);
                                })
                                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST))
                )
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @DeleteMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteSale(@PathVariable("s") int idSale) {
        return saleRepository.findById(idSale)
                .map( (sale) -> {
                    saleCanceledRepository.save(new SaleCanceled(sale));
                    saleRepository.deleteById(idSale);
                    return new ResponseEntity( OK);
                })
                .orElseGet( () -> new ResponseEntity(BAD_REQUEST));
    }
}
