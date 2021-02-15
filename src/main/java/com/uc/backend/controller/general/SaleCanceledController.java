package com.uc.backend.controller.general;

import com.uc.backend.entity.Sale;
import com.uc.backend.entity.SaleCanceled;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.SaleCanceledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("model/sale-canceled")
public class SaleCanceledController {

    SaleCanceledRepository saleCanceledRepository;

    @Autowired
    public SaleCanceledController(SaleCanceledRepository saleCanceledRepository){
        this.saleCanceledRepository = saleCanceledRepository;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SaleCanceled>> getAll(@RequestParam(name = "u", required = false) UniversityName u,
                                             @RequestParam(name = "s", required = false) LevelfyServiceType s,
                                             @RequestParam(name = "p", required = false) Boolean solved){

        List<SaleCanceled> saleCanceledList;

        if (u!=null && s!=null) saleCanceledList = saleCanceledRepository.findSaleCanceledsByCourse_CourseId_UniversityAndServiceType(u, s);
        else if(u!=null) saleCanceledList = saleCanceledRepository.findSaleCanceledsByCourse_CourseId_University(u);
        else if(s!=null) saleCanceledList = saleCanceledRepository.findSaleCanceledsByServiceType(s);
        else saleCanceledList = saleCanceledRepository.findAll();

        if (solved == null) return new ResponseEntity<>(saleCanceledList, HttpStatus.OK);

        if (solved)
            return new ResponseEntity<>(saleCanceledList.stream().filter(SaleCanceled::getSolved).collect(Collectors.toList()), HttpStatus.OK);
        else
            return new ResponseEntity<>(saleCanceledList.stream().filter(sc->!sc.getSolved()).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleCanceled> getSale(@PathVariable("s") int idSale){
        return saleCanceledRepository.findById(idSale)
                .map( (sale) -> new ResponseEntity<>(sale,OK))
                .orElseGet( () -> new ResponseEntity<>(null,BAD_REQUEST));
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleCanceled> newSale(@RequestBody SaleCanceled saleCanceled) {
        return saleCanceledRepository.findById(saleCanceled.getId())
                .map( value-> new ResponseEntity<>(value, BAD_REQUEST))
                .orElseGet( () ->  new ResponseEntity<>(saleCanceledRepository.save(saleCanceled),OK));
    }


    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleCanceled> updateSale(@RequestBody SaleCanceled saleCanceled) {
        return saleCanceledRepository.findById(saleCanceled.getId())
                .map( s -> new ResponseEntity<>(saleCanceledRepository.save(saleCanceled), OK))
                .orElseGet( () -> new ResponseEntity<>(null, BAD_REQUEST));
    }

    @DeleteMapping(value = "{s}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSale(@PathVariable("s") int idSale) {
        return saleCanceledRepository.findById(idSale)
                .map( (sale) -> {
                    saleCanceledRepository.deleteById(idSale);
                    return new ResponseEntity<>("Successfully deleted", OK);
                })
                .orElseGet( () -> new ResponseEntity<>("Error: object doesn't exist", BAD_REQUEST));
    }

}
