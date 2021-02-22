package com.uc.backend.repository;

import com.uc.backend.entity.ServiceAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAgendaRepository extends JpaRepository<ServiceAgenda, Integer> {

}
