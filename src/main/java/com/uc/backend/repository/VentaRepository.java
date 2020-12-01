package com.uc.backend.repository;

import com.uc.backend.entity.Clase;
import com.uc.backend.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    Optional<Venta> findVentaByClaseEnroll_IdClaseEnroll(int idclase);

    List<Venta> findVentasByClaseEnroll_IdClaseEnroll(int id);

    List<Venta> findVentasByClaseEnroll_Clase(Clase claseEnroll_clase);

}
