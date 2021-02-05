package com.uc.backend.controller.mongo;

import com.uc.backend.mongo.document.prices.AsesPaqPriceDocument;
import com.uc.backend.mongo.document.prices.AsesPerPriceDocument;
import com.uc.backend.mongo.document.prices.MarPriceDocument;
import com.uc.backend.mongo.repository.AsesPaqPriceRepository;
import com.uc.backend.mongo.repository.AsesPerPriceRepository;
import com.uc.backend.mongo.repository.MarPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("levelfy-service/price")
public class LevelfyServicePriceController {

    AsesPerPriceRepository asesPerPriceRepository;
    AsesPaqPriceRepository asesPaqPriceRepository;
    MarPriceRepository marPriceRepository;

    @Autowired
    public LevelfyServicePriceController(AsesPerPriceRepository asesPerPriceRepository,
                                         AsesPaqPriceRepository asesPaqPriceRepository,
                                         MarPriceRepository marPriceRepository){
        this.asesPaqPriceRepository = asesPaqPriceRepository;
        this.asesPerPriceRepository = asesPerPriceRepository;
        this.marPriceRepository = marPriceRepository;
    }


    @GetMapping(value = "ases-paq", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AsesPaqPriceDocument>> getAsesPaqPriceDocument(){
        return new ResponseEntity<>(asesPaqPriceRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping(value = "ases-per", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AsesPerPriceDocument>> getAsesPerPriceDocument(){
        return new ResponseEntity<>(asesPerPriceRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping(value = "mar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MarPriceDocument>> getMarPriceDocument(){
        return new ResponseEntity<>(marPriceRepository.findAll(),HttpStatus.OK);
    }



    /*
    @Autowired
    MongoCourseRepository mongoCourseRepository;

    @GetMapping(value = "course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MongoCourse>> getCourses() {
        return new ResponseEntity<>(mongoCourseRepository.findAll(), HttpStatus.OK
        );
    }
*/


}
