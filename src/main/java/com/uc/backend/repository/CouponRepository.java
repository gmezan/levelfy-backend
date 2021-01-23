package com.uc.backend.repository;

import com.uc.backend.model.CommentForum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CommentForum, Integer> {
/*

    @Query(value= "SELECT activo, valor FROM cupon where idcupones = ?1 ", nativeQuery = true)
    String validarCupon(String cupon);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE usuario set cupon =?1 where idUsuario =?2 ", nativeQuery = true)
    void agregarCupon(String cupon, String idusuario);*/
}
