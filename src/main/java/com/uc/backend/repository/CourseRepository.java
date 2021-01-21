package com.uc.backend.repository;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, CourseId> {

    /*
    List<Course> findAll();

    List<Course> findAllByCursoId_Universidad(String cursoId_universidad);
    @Query(value="SELECT distinct universidad  FROM universityclass.curso",nativeQuery = true)
    List<String> findUniversidades();


     */

    // Available must be true
    @Query(value = "select distinct c.* from curso c inner join clase cl on (cl.idcurso = c.idcurso)\n" +
            "where cl.tipo_servicio = ?1 and cl.disponible=1", nativeQuery = true)
    List<Course> findCoursesAvailableByServiceType(String serviceType);

}
