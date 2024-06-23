package com.upc.FinanazasGrupo4.service.impl;

import com.upc.FinanazasGrupo4.resource.dto.CustomerResource;
import com.upc.FinanazasGrupo4.shared.exception.ValidationException;
import com.upc.FinanazasGrupo4.domain.model.Customer;
import com.upc.FinanazasGrupo4.domain.persistence.repository.CustomerRepository;
import com.upc.FinanazasGrupo4.domain.service.inter.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CustomerServiceImpl(){
        this.modelMapper = new ModelMapper();
    }

    private CustomerResource EntityToDto(Customer customer){
        return modelMapper.map(customer, CustomerResource.class);
    }

    private Customer DtoToEntity(CustomerResource customerResource){
        return modelMapper.map(customerResource, Customer.class);
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer createCustomer(CustomerResource customerResource) {

        validationCustomer(customerResource);

        return customerRepository.save(DtoToEntity(customerResource));
    }

    @Override
    public Customer updateCustomer(Long idCustomer, CustomerResource customerResource) {

        Customer customer = customerRepository.findById(idCustomer).orElseThrow(() -> new ValidationException("No existe el customer"));
        validationCustomer(customerResource);

        customer.setEmail(customerResource.getEmail());
        customer.setPassword(customerResource.getPassword());
        customer.setName(customerResource.getName());
        customer.setLastName(customerResource.getLastName());
        customer.setDni(customerResource.getDni());

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerByEmailAndPassword(String email, String password) {
        return customerRepository.getCustomerByEmailAndPassword(email,password);
    }

    @Override
    public void deleteCustomer(Long id) {

        if(!customerRepository.existsById(id)){
            throw new ValidationException("No existe el customer");
        }

        customerRepository.deleteById(id);
    }

    private void validationCustomer(CustomerResource customerResource){

        if (customerResource.getEmail() == null || customerResource.getEmail().isEmpty()) {
            throw new ValidationException("El email no puede ser nulo o vacio");
        }

        if(customerResource.getPassword() == null || customerResource.getPassword().isEmpty()){
            throw new ValidationException("El password no puede ser nulo o vacio");
        }

        if(customerResource.getName() == null || customerResource.getName().isEmpty()){
            throw new ValidationException("El nombre no puede ser nulo o vacio");
        }

        if(customerResource.getLastName() == null || customerResource.getLastName().isEmpty()){
            throw new ValidationException("El apellido no puede ser nulo o vacio");
        }

        if(customerResource.getDni() == null || customerResource.getDni().isEmpty()){
            throw new ValidationException("El dni no puede ser nulo o vacio");
        }

        if(customerRepository.existsByEmail(customerResource.getEmail())){
            throw new ValidationException("El email "+ customerResource.getEmail()+" ya existe");
        }

    }

}
