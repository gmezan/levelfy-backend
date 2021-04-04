package com.uc.backend.repository;

import com.uc.backend.entity.ServiceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceSessionRepository extends JpaRepository <ServiceSession, Integer> {

    /*
    //List<ClaseSesion> findAllByPaqueteAsesoria_IdpaqueteasesoriaOrderByFechahoraAsc(int idpaqueteasesoria);
    List<ServiceSession> findByClase_IdclaseOrderByFechaAscInicioAsc(int idClase);

    List<ServiceSession> findByClase_IdclaseOrderByFechaDescInicioDesc(int idClase);

    List<ServiceSession> findByClase_Idclase(int idclase);
*/

    List<ServiceSession> findServiceSessionsByService_IdService(int service_idService);
}
