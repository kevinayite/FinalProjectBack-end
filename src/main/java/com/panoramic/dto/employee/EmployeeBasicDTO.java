package com.panoramic.dto.employee;

import com.panoramic.dto.department.DepartmentBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBasicDTO {

    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private DepartmentBasicDTO department;
}
