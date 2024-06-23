package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.domain.model.Informacion;

import java.util.List;

public interface InformacionService {

    List<Informacion> getAllInformaciones();

    Informacion createInformacion(Informacion informacion);

    void deleteInformacion(Long id);



}
