package com.uc.backend.controller;

import com.uc.backend.repository.CuponesRespository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CuponesController {

    @Autowired
    CuponesRespository cuponesRespository;

    @ResponseBody
    @GetMapping(value="/validarCupon/{cupon}")
    public ResponseEntity validarCupon(@PathVariable("cupon") String cupon){
        Gson gson = new Gson();
        return new ResponseEntity(cuponesRespository.validarCupon(cupon), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value="/agregarCupon/{idUsuario}/{cupon}")
    public ResponseEntity agregar(@PathVariable("idUsuario") String idUsuario,
                                  @PathVariable("cupon") String cupon){
        cuponesRespository.agregarCupon(cupon,idUsuario);
        return new ResponseEntity(HttpStatus.OK);
    }

}
