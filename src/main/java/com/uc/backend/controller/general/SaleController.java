package com.uc.backend.controller.general;

import com.uc.backend.entity.Sale;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("model/sale")
public class SaleController {

    @Autowired
    SaleRepository saleRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sale>> getAll(){
        return new ResponseEntity<>(saleRepository.findAll(), HttpStatus.OK);
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
                                .map( (enrr) -> {
                                    sale.setEnrollment(enrr);
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
                                .map( (enrr) -> {
                                    sale.setEnrollment(enrr);
                                    return new ResponseEntity<>(saleRepository.save(sale),OK);
                                })
                                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST))
                )
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @DeleteMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSale(@PathVariable("s") int idSale) {
        return saleRepository.findById(idSale)
                .map( (sale) -> {
                    saleRepository.deleteById(idSale);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( () -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }
}
