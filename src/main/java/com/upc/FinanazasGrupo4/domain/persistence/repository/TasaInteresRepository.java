package com.upc.FinanazasGrupo4.domain.persistence.repository;

import com.upc.FinanazasGrupo4.domain.model.TasaInteres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasaInteresRepository extends JpaRepository<TasaInteres, Long> {
    TasaInteres findByTipoAndPlazo(String tipo, String plazo);
}
