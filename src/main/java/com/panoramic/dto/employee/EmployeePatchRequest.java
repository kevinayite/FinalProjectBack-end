package com.panoramic.dto.employee;

import com.panoramic.model.enums.EmploymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeePatchRequest {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Long departmentId;
    private String position;
    private LocalDate joinDate;
    private EmploymentType employmentType;
    private BigDecimal salary;
    private Boolean isActive;
    private Boolean clearResume;
    private Boolean clearIdProof;
}
