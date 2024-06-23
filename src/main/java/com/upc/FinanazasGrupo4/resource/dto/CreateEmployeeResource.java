package com.upc.FinanazasGrupo4.resource.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateEmployeeResource {
    public String name;
    public String lastName;
    public String dni;
    public String email;
    public String password;

}
