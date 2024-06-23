package com.upc.FinanazasGrupo4.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    public String name;
    @Column(name = "lastname", length = 100, nullable = false)
    public String lastname;
    @Column(name = "dni", length = 100, nullable = false)
    public String dni;
    @Column(name = "email", length = 100, nullable = false)
    public String email;
    @Column(name = "password", length = 100, nullable = false)
    public String password;
}
