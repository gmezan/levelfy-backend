package com.uc.backend.repository;

import com.uc.backend.entity.Clase;
import com.uc.backend.entity.ClaseEnroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClaseEnrollRepository extends JpaRepository<ClaseEnroll, Integer> {

    // Para los estudiantes:

    List<ClaseEnroll> findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrue(Integer iduser, String servicio);

    //findClaseEnrollsByEstudiante_IdusuarioAndActivoIsTrueAndClase_Idclase
    List<ClaseEnroll> findClaseEnrollsByEstudiante_IdusuarioAndClase_IdclaseAndClase_DisponibleIsTrue(int iduser, int idclase);

    List<ClaseEnroll> findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrue(int clase_profesor_idusuario, String clase_servicio);

    List<ClaseEnroll> findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClaseDisponibleIsTrueAndInicioasesoriaIsAfter(int clase_profesor_idusuario, String clase_servicio, LocalDateTime dt);

    Optional<ClaseEnroll> findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio(int idClaseEnroll, int estudiante_idusuario, String servicio);

    List<ClaseEnroll> findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(int estudiante_idusuario, String clase_servicio);

    Optional<ClaseEnroll> findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(int clase_idclase, int estudiante_idusuario);
    Optional<ClaseEnroll> findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrueAndPagadoIsTrue(int clase_idclase, int estudiante_idusuario);
    List<ClaseEnroll> findClaseEnrollByClase_Idclase(int idClase);

    Optional<ClaseEnroll> findClaseEnrollByActiveIsTrueAndClase_Profesor_IdusuarioAndIdClaseEnrollAndClase_Idclase(int clase_profesor_idusuario, int idClaseEnroll, int clase_idclase);

    List<ClaseEnroll> findClaseEnrollsByActiveIsTrueAndClase_DisponibleIsFalseAndClase_Servicio(String clase_servicio);

    List<ClaseEnroll> findClaseEnrollsByActiveIsTrueAndClase_Servicio(String clase_servicio);



}
