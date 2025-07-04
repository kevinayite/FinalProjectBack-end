//package com.panoramic.dto.employee;
//
//import com.panoramic.model.enums.EmploymentType;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Data
//public class EmployeeResponse {
//    private Long id;
//    private String employeeId;
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String phoneNumber;
//    private LocalDate dateOfBirth;
//    private String gender;
//    private String departmentName;
//    private String position;
//    private LocalDate joinDate;
//    private EmploymentType employmentType;
//    private BigDecimal salary;
//    private Boolean isActive;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//}


package com.panoramic.dto.employee;

import com.panoramic.model.enums.EmploymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class EmployeeResponse {
    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String departmentName; // Display department name directly
    private String position;
    private LocalDate joinDate;
    private EmploymentType employmentType;
    private BigDecimal salary;
    private Boolean isActive;
    private String userName; // Display user name directly
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Attributes for Resume File (for displaying/downloading) ---
    private String resumeOriginalFilename; // Original filename
    private String resumeContentType;      // MIME type
    private Long resumeSize;               // File size
    private String resumeDownloadUrl;      // URL to download the resume

    // --- Attributes for ID Proof File (for displaying/downloading) ---
    private String idProofOriginalFilename; // Original filename
    private String idProofContentType;      // MIME type
    private Long idProofSize;               // File size
    private String idProofDownloadUrl;      // URL to download the ID proof
}