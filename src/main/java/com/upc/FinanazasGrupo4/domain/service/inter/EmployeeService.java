package com.upc.FinanazasGrupo4.domain.service.inter;

import com.upc.FinanazasGrupo4.domain.model.Employee;
import com.upc.FinanazasGrupo4.resource.dto.CreateEmployeeResource;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployee();
    Optional<Employee> getById(Long employeeId);

    Employee createEmployee(CreateEmployeeResource createEmployeeResource);

    Employee getEmployeeByEmailAndPassword(String email, String password);

    void deleteEmployee(Long id);
}
