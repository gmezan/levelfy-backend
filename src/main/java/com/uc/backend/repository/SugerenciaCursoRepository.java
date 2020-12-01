package com.uc.backend.repository;

import com.uc.backend.entity.SugerenciaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SugerenciaCursoRepository extends JpaRepository<SugerenciaCurso, Integer> {


}
