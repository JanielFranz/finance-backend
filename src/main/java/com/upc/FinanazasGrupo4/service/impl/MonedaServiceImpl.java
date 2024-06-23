package com.upc.FinanazasGrupo4.service.impl;

import com.upc.FinanazasGrupo4.domain.model.Moneda;
import com.upc.FinanazasGrupo4.domain.persistence.repository.MonedaRepository;
import com.upc.FinanazasGrupo4.domain.service.inter.MonedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonedaServiceImpl implements MonedaService {

    @Autowired
    MonedaRepository monedaRepository;

    @Override
    public List<Moneda> getAllMonedas() {
        return monedaRepository.findAll();
    }
}
