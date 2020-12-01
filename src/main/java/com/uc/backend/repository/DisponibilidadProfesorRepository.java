package com.uc.backend.repository;

import com.uc.backend.entity.DisponibilidadProfesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisponibilidadProfesorRepository extends JpaRepository<DisponibilidadProfesor,Integer> {

    List<DisponibilidadProfesor> findDisponibilidadProfesorsByUsuario_IdusuarioAndUsuario_ActivoIsTrueAndUsuario_Rol_Idrol(int idasesor, int idrol);

    List<DisponibilidadProfesor> findDisponibilidadProfesorsByUsuario_IdusuarioAndDia(int usuario_idusuario, int dia);

    Optional<DisponibilidadProfesor> findDisponibilidadProfesorByIdAndUsuario_Idusuario(Integer integer, Integer idprofe);

    Optional<DisponibilidadProfesor> findDisponibilidadProfesorsByIdAndUsuario_IdusuarioAndDia(int id, int usuario_idusuario,  Integer dia);

}
