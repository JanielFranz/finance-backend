package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.domain.model.Cuota;

import java.util.List;

public interface CuotaService {

    List<Cuota> getAllCuotas();

    Cuota createCuota(Cuota cuota);
}

