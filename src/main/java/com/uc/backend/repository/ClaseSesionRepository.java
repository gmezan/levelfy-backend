package com.uc.backend.repository;

import com.uc.backend.entity.ClaseSesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaseSesionRepository extends JpaRepository <ClaseSesion, Integer> {

    //List<ClaseSesion> findAllByPaqueteAsesoria_IdpaqueteasesoriaOrderByFechahoraAsc(int idpaqueteasesoria);
    List<ClaseSesion> findByClase_IdclaseOrderByFechaAscInicioAsc(int idClase);

    List<ClaseSesion> findByClase_IdclaseOrderByFechaDescInicioDesc(int idClase);

    List<ClaseSesion> findByClase_Idclase(int idclase);

}
