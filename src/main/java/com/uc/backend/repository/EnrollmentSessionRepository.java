package com.uc.backend.repository;

import com.uc.backend.entity.EnrollmentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentSessionRepository extends JpaRepository <EnrollmentSession, Integer> {

    /*
    //List<ClaseSesion> findAllByPaqueteAsesoria_IdpaqueteasesoriaOrderByFechahoraAsc(int idpaqueteasesoria);
    List<EnrollmentSession> findByClase_IdclaseOrderByFechaAscInicioAsc(int idClase);

    List<EnrollmentSession> findByClase_IdclaseOrderByFechaDescInicioDesc(int idClase);

    List<EnrollmentSession> findByClase_Idclase(int idclase);
*/
}
