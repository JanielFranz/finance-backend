package com.upc.FinanazasGrupo4.service.impl;

import com.upc.FinanazasGrupo4.resource.dto.TasaInteresResource;
import com.upc.FinanazasGrupo4.shared.exception.ValidationException;
import com.upc.FinanazasGrupo4.domain.model.TasaInteres;
import com.upc.FinanazasGrupo4.domain.persistence.repository.TasaInteresRepository;
import com.upc.FinanazasGrupo4.domain.service.inter.TasaInteresService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Service
public class TasaInteresServiceImpl implements TasaInteresService {

    @Autowired
    private TasaInteresRepository tasaInteresRepository;

    @Autowired
    private ModelMapper modelMapper;

    public TasaInteresServiceImpl(){
        this.modelMapper = new ModelMapper();
    }

    private TasaInteresResource EntityToDto(TasaInteres tasaInteres){
        return modelMapper.map(tasaInteres, TasaInteresResource.class);
    }

    private TasaInteres DtoToEntity(TasaInteresResource tasaInteresResource){
        return modelMapper.map(tasaInteresResource, TasaInteres.class);
    }

    @Override
    public List<TasaInteres> getAllTasaInteres() {

        return tasaInteresRepository.findAll();
    }

    @Override
    public TasaInteres createTasaInteres(TasaInteresResource tasaInteresResource) {
        validationTasaInteres(tasaInteresResource);
        return tasaInteresRepository.save(DtoToEntity(tasaInteresResource));
    }

    @Override
    public void deleteTasaInteres(Long id) {
        tasaInteresRepository.deleteById(id);
    }

    private void validationTasaInteres(TasaInteresResource tasaInteresResource){

        if (tasaInteresResource.tipo == null || tasaInteresResource.tipo.isEmpty()) {
            throw new ValidationException("El tipo de tasa de interes no puede ser nulo o vacio");
        }
        if (tasaInteresResource.plazo == null || tasaInteresResource.plazo.isEmpty()) {
            throw new ValidationException("El plazo de tasa de interes no puede ser nulo o vacio");
        }
        if (tasaInteresResource.abreviatura == null || tasaInteresResource.abreviatura.isEmpty()) {
            throw new ValidationException("La abreviatura de tasa de interes no puede ser nulo o vacio");
        }

    }
}
