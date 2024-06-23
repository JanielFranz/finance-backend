package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.resource.dto.CustomerResource;
import com.upc.FinanazasGrupo4.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Optional<Customer> getById(Long customerId);

    Customer createCustomer(CustomerResource customerResource);

    Customer updateCustomer(Long id, CustomerResource customerResource);
    Customer getCustomerByEmailAndPassword(String email, String password);

    void deleteCustomer(Long id);
}
