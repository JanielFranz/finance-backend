package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.resource.dto.CreateEntryDataCronograma;
import com.upc.FinanazasGrupo4.domain.model.Cronograma;
import com.upc.FinanazasGrupo4.domain.model.Cuota;
import com.upc.FinanazasGrupo4.domain.model.Informacion;

import java.util.List;

public interface CronogramaService {

    List<Cronograma> getAllCronogramas();
    List<Cuota>getAllCuotasByCronograma(Long id);
    Informacion getInformacionByCronograma(Long id);
    void deleteCronograma(Long id);
    Cronograma saveCronograma(Long customerId, CreateEntryDataCronograma variablesCalculo);

    List<Cronograma> getAllCronogramasByCustomerId(Long customerId);

    List<Cronograma> getAllCronogramaByCustomerIdAndId(Long customerId, Long Id);
}
