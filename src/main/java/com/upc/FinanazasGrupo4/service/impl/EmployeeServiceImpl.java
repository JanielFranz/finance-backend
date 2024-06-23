package com.upc.FinanazasGrupo4.service.impl;

import com.upc.FinanazasGrupo4.domain.model.Employee;
import com.upc.FinanazasGrupo4.domain.persistence.repository.EmployeeRepository;
import com.upc.FinanazasGrupo4.domain.service.inter.EmployeeService;
import com.upc.FinanazasGrupo4.resource.dto.CreateEmployeeResource;
import com.upc.FinanazasGrupo4.shared.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public EmployeeServiceImpl(){
        this.modelMapper = new ModelMapper();
    }

    private CreateEmployeeResource EntityToDto(Employee employee){
        return modelMapper.map(employee, CreateEmployeeResource.class);
    }

    private Employee DtoToEntity(CreateEmployeeResource createEmployeeResource){
        return modelMapper.map(createEmployeeResource, Employee.class);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public Employee createEmployee(CreateEmployeeResource createEmployeeResource) {

        return employeeRepository.save(DtoToEntity(createEmployeeResource));
    }


    @Override
    public Employee getEmployeeByEmailAndPassword(String email, String password) {
        return employeeRepository.getEmployeeByEmailAndPassword(email, password);
    }

    @Override
    public void deleteEmployee(Long id) {

        if(!employeeRepository.existsById(id)){
            throw new ValidationException("No existe el customer");
        }

        employeeRepository.deleteById(id);
    }
}
