package com.uc.backend.repository;

import com.uc.backend.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // Para los estudiantes:

    /*
    List<Enrollment> findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrue(Integer iduser, String servicio);

    //findClaseEnrollsByEstudiante_IdusuarioAndActivoIsTrueAndClase_Idclase
    List<Enrollment> findClaseEnrollsByEstudiante_IdusuarioAndClase_IdclaseAndClase_DisponibleIsTrue(int iduser, int idclase);

    List<Enrollment> findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrue(int clase_profesor_idusuario, String clase_servicio);

    List<Enrollment> findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClaseDisponibleIsTrueAndInicioasesoriaIsAfter(int clase_profesor_idusuario, String clase_servicio, LocalDateTime dt);

    Optional<Enrollment> findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio(int idClaseEnroll, int estudiante_idusuario, String servicio);

    List<Enrollment> findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(int estudiante_idusuario, String clase_servicio);

    Optional<Enrollment> findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(int clase_idclase, int estudiante_idusuario);
    Optional<Enrollment> findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrueAndPagadoIsTrue(int clase_idclase, int estudiante_idusuario);
    List<Enrollment> findClaseEnrollByClase_Idclase(int idClase);

    Optional<Enrollment> findClaseEnrollByActiveIsTrueAndClase_Profesor_IdusuarioAndIdClaseEnrollAndClase_Idclase(int clase_profesor_idusuario, int idClaseEnroll, int clase_idclase);

    List<Enrollment> findClaseEnrollsByActiveIsTrueAndClase_DisponibleIsFalseAndClase_Servicio(String clase_servicio);

    List<Enrollment> findClaseEnrollsByActiveIsTrueAndClase_Servicio(String clase_servicio);

*/

}
