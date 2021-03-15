package com.uc.backend.repository;

import com.uc.backend.dto.CourseId;
import com.uc.backend.entity.Course;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
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
    @Query(value = "select distinct c.* from curso c inner join clase cl on (cl.idcurso = c.idcurso and cl.universidad = c.universidad) " +
            "where cl.tipo_servicio = \"ASES_PAQ\" and cl.disponible=1", nativeQuery = true)
    List<Course> findCoursesAvailableByServiceTypeAndAvailableIsTrue(String serviceType);

    List<Course> findCourseByCourseId_University(UniversityName courseId_university);

}
