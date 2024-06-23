package com.upc.FinanazasGrupo4.domain.persistence.repository;

import com.upc.FinanazasGrupo4.domain.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {
}
