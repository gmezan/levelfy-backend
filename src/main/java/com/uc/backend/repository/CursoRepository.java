package com.uc.backend.repository;

import com.uc.backend.dto.CursoId;
import com.uc.backend.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, CursoId> {

    List<Curso> findAll();

    List<Curso> findAllByCursoId_Universidad(String cursoId_universidad);
    @Query(value="SELECT distinct universidad  FROM universityclass.curso",nativeQuery = true)
    List<String> findUniversidades();

}
