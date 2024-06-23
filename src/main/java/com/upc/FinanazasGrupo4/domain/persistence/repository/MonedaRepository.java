package com.upc.FinanazasGrupo4.domain.persistence.repository;

import com.upc.FinanazasGrupo4.domain.model.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonedaRepository extends JpaRepository<Moneda, Long> {

    Moneda findByNombre(String nombre);
}
