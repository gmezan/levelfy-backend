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
}
