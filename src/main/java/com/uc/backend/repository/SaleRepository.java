package com.uc.backend.repository;

import com.uc.backend.entity.Service;
import com.uc.backend.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    /*
    Optional<Sale> findVentaByClaseEnroll_IdClaseEnroll(int idclase);

    List<Sale> findVentasByClaseEnroll_IdClaseEnroll(int id);

    List<Sale> findVentasByClaseEnroll_Clase(Service claseEnroll_service);


     */
}
