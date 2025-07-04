//package com.panoramic.dto.employee;
//
//import com.panoramic.model.enums.EmploymentType;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Data
//public class EmployeeRequest {
//    @NotBlank(message = "First name is required")
//    private String firstName;
//
//    @NotBlank(message = "Last name is required")
//    private String lastName;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    private String email;
//
//    private String phoneNumber;
//
//    private LocalDate dateOfBirth;
//
//    private String gender;
//
//    @NotNull(message = "Department is required")
//    private Long departmentId;
//
//    @NotBlank(message = "Position is required")
//
//    private String position;
//
//    @NotNull(message = "Join date is required")
//    private LocalDate joinDate;
//
//    @NotNull(message = "Employment type is required")
//    private EmploymentType employmentType;
//
//    @NotNull(message = "Salary is required")
//    private BigDecimal salary;
//
//    @NotBlank(message = "Employee ID is required")
//    private String employeeId;
//    @NotNull(message = "Resume is required")
//    private MultipartFile resume;
//    @NotNull(message = "ID is required")
//    private MultipartFile idProof;
//}


package com.panoramic.dto.employee;

import com.panoramic.model.enums.EmploymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class EmployeeRequest {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Long departmentId; // Will receive department ID to link
    private String position;
    private LocalDate joinDate;
    private EmploymentType employmentType;
    private BigDecimal salary;
    private Boolean isActive;
    private Long userId; // Will receive user ID to link
    private Boolean clearResume = false;
    private Boolean clearIdProof = false;


}