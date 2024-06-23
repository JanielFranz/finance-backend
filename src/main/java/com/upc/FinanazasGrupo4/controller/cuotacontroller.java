package com.upc.FinanazasGrupo4.controller;

import com.upc.FinanazasGrupo4.resource.dto.CreateEntryDataCronograma;
import com.upc.FinanazasGrupo4.domain.model.Cronograma;
import com.upc.FinanazasGrupo4.domain.service.inter.CronogramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/finanzasgrupo4/v1/cuota")
public class cuotacontroller {

    @Autowired
    private CronogramaService cronogramaService;

    @Transactional
    @GetMapping("/{customerId}/cuota")
    public ResponseEntity<List<Cronograma>> getAllCronogramasByCustomerId(@PathVariable Long customerId){
        return new ResponseEntity<>(cronogramaService.getAllCronogramasByCustomerId(customerId), org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("/{customerId}/hallarcuota")
    public ResponseEntity<Cronograma> calcularCronograma(@PathVariable Long customerId, @RequestBody CreateEntryDataCronograma createEntryDataCronograma){
        return new ResponseEntity<>(cronogramaService.saveCronograma(customerId, createEntryDataCronograma),org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/id/{customerId}/cronograma/{id}")
    public List<Cronograma> getCronogramaByCustomerIdAndId(@PathVariable Long customerId, @PathVariable Long id) {
        return cronogramaService.getAllCronogramaByCustomerIdAndId(customerId, id);
    }


}
