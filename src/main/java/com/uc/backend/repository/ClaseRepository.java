package com.uc.backend.repository;

import com.uc.backend.dto.CursoId;
import com.uc.backend.entity.Clase;
import com.uc.backend.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    List<Clase> findClasesByServicioAndDisponibleIsTrue(String service);

    Optional<Clase> findByIdclaseAndDisponibleIsTrue(int id);

    Optional<Clase> findByIdclaseAndServicioAndDisponibleIsTrue(int id,String service);

    Optional<Clase> findByIdclaseAndProfesor_IdusuarioAndServicioAndDisponibleIsTrue(int idclase, int profesor_idusuario, String servicio);

    //Para los asesores /a
    List<Clase> findClasesByProfesor_IdusuarioAndDisponibleIsTrueAndServicio(int profesor_idusuario, String service);

    List<Clase> findClasesByProfesor_IdusuarioAndDisponibleIsFalseAndServicio(int profesor_idusuario, String service);

    Optional<Clase> findByIdclaseAndProfesor_IdusuarioAndServicio(int idclase, int profesor_idusuario, String servicio);
    Optional<Clase> findByIdclaseAndServicio(int idclase, String servicio);

    Optional<Clase> findByIdclaseAndProfesor_IdusuarioAndServicioAndArchivedIsFalse(int idclase, int profesor_idusuario, String servicio);

    Optional<Clase> findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndArchivedIsFalse(int profesor_idusuario, String servicio, CursoId curso_cursoId);

    Optional<Clase> findByProfesor_IdusuarioAndServicioAndCurso_CursoIdAndEvaluacionAndDisponibleIsTrue(int profesor_idusuario, String servicio, CursoId curso_cursoId, Integer evaluacion);

    List<Clase> findClasesByServicioAndDisponibleIsTrueAndCurso_CursoId_Universidad(String servicio, String curso_cursoId_universidad);

}
