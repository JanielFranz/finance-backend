package com.upc.FinanazasGrupo4.domain.persistence.repository;

import com.upc.FinanazasGrupo4.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee getEmployeeByEmailAndPassword(String email, String password);

}
