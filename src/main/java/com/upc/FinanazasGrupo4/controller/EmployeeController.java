package com.upc.FinanazasGrupo4.controller;

import com.upc.FinanazasGrupo4.domain.model.Employee;
import com.upc.FinanazasGrupo4.domain.service.inter.EmployeeService;
import com.upc.FinanazasGrupo4.resource.dto.CreateEmployeeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/finanzasgrupo4/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Transactional(readOnly = true)
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees(){
        return new ResponseEntity<>(employeeService.getAllEmployee(), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/email/{email}/password/{password}")
    public ResponseEntity<Employee> getByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return new ResponseEntity<>(employeeService.getEmployeeByEmailAndPassword(email, password), org.springframework.http.HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Employee> createCustomer(@RequestBody CreateEmployeeResource createEmployeeResource){
        return new ResponseEntity<>(employeeService.createEmployee(createEmployeeResource), org.springframework.http.HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(org.springframework.http.HttpStatus.NO_CONTENT);
    }

    @GetMapping("/id/{employeeId}")
    public Optional<Employee> getById(@PathVariable Long employeeId){
        return new ResponseEntity<>(employeeService.getById(employeeId), HttpStatus.OK).getBody();
    }
}
