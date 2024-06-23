package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.resource.dto.TasaInteresResource;
import com.upc.FinanazasGrupo4.domain.model.TasaInteres;

import java.util.List;

public interface TasaInteresService {

    List<TasaInteres> getAllTasaInteres();

     TasaInteres createTasaInteres(TasaInteresResource tasaInteres);

        void deleteTasaInteres(Long id);

}
