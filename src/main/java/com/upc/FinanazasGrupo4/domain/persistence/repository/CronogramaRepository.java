package com.upc.FinanazasGrupo4.domain.persistence.repository;

import com.upc.FinanazasGrupo4.domain.model.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
    List<Cronograma> findAllByCustomerId(Long customerId);

    List<Cronograma> findAllByCustomerIdAndId(Long customerId, Long id);}
