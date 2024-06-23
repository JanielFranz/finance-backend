package com.upc.FinanazasGrupo4.controller;

import com.upc.FinanazasGrupo4.resource.dto.CustomerResource;
import com.upc.FinanazasGrupo4.domain.model.Customer;
import com.upc.FinanazasGrupo4.domain.service.inter.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/finanzasgrupo4/v1/Customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Transactional(readOnly = true)
    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<>(customerService.getAllCustomers(), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/email/{email}/password/{password}")
    public ResponseEntity<Customer> getByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return new ResponseEntity<>(customerService.getCustomerByEmailAndPassword(email, password), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/id/{customerId}")
    public Optional<Customer> getById(@PathVariable Long customerId){
        return new ResponseEntity<>(customerService.getById(customerId), HttpStatus.OK).getBody();
    }

    @PostMapping()
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerResource customerResource){
        return new ResponseEntity<>(customerService.createCustomer(customerResource), org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,@RequestBody CustomerResource customerResource){
        return new ResponseEntity<>(customerService.updateCustomer(id, customerResource), org.springframework.http.HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(org.springframework.http.HttpStatus.NO_CONTENT);
    }


}
