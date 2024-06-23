package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.resource.dto.VehiculoResource;
import com.upc.FinanazasGrupo4.domain.model.Vehiculo;

import java.util.List;

public interface VehiculoService {

    List<Vehiculo> getAllVehiculos();
    Vehiculo createVehiculo(VehiculoResource vehiculoResource);
    void deleteVehiculo(Long id);

}
