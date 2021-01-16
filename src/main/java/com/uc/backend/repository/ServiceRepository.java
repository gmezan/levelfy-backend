package com.uc.backend.repository;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    /*
    List<Service> findClasesByServicioAndDisponibleIsTrue(String service);

    Optional<Service> findByIdclaseAndDisponibleIsTrue(int id);

    Optional<Service> findByIdclaseAndServicioAndDisponibleIsTrue(int id, String service);

    Optional<Service> findByIdclaseAndProfesor_IdusuarioAndServicioAndDisponibleIsTrue(int idclase, int profesor_idusuario, String servicio);

    //Para los asesores /a
    List<Service> findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(int profesor_idusuario, String service);

    List<Service> findClasesByProfesor_IdusuarioAndDisponibleIsFalseAndServicio(int profesor_idusuario, String service);

    Optional<Service> findByIdclaseAndProfesor_IdusuarioAndServicio(int idclase, int profesor_idusuario, String servicio);
    Optional<Service> findByIdclaseAndServicio(int idclase, String servicio);

    Optional<Service> findByIdclaseAndProfesor_IdusuarioAndServicioAndArchivedIsFalse(int idclase, int profesor_idusuario, String servicio);

    Optional<Service> findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndArchivedIsFalse(int profesor_idusuario, String servicio, CourseId curso_courseId);

    Optional<Service> findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndEvaluacionAndDisponibleIsTrue(int profesor_idusuario, String servicio, CourseId curso_courseId, Integer evaluacion);

    List<Service> findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(String servicio, String curso_cursoId_universidad);
*/
}
