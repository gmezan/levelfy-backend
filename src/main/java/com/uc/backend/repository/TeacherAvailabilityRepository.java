package com.uc.backend.repository;

import com.uc.backend.model.TeacherAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherAvailabilityRepository extends JpaRepository<TeacherAvailability,Integer> {

    /*
    List<TeacherAvailability> findDisponibilidadProfesorsByUsuario_IdusuarioAndUsuario_ActivoIsTrueAndUsuario_Rol_Idrol(int idasesor, int idrol);

    List<TeacherAvailability> findDisponibilidadProfesorsByUsuario_IdusuarioAndDia(int usuario_idusuario, int dia);

    Optional<TeacherAvailability> findDisponibilidadProfesorByIdAndUsuario_Idusuario(Integer integer, Integer idprofe);

    Optional<TeacherAvailability> findDisponibilidadProfesorsByIdAndUsuario_IdusuarioAndDia(int id, int usuario_idusuario, Integer dia);


     */
}
