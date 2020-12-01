package com.uc.backend.repository;

import com.uc.backend.entity.ComentarioForo;
import com.uc.backend.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CuponesRespository extends JpaRepository<ComentarioForo, Integer> {


    @Query(value= "SELECT activo, valor FROM cupon where idcupones = ?1 ", nativeQuery = true)
    String validarCupon(String cupon);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE usuario set cupon =?1 where idUsuario =?2 ", nativeQuery = true)
    void agregarCupon(String cupon, String idusuario);
}
