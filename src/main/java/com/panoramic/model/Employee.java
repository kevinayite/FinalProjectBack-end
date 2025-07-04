//package com.panoramic.model;
//
//import com.panoramic.model.enums.EmploymentType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "employees")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "employee_id", unique = true, nullable = false)
//    private String employeeId;
//
//    @Column(name = "first_name", nullable = false)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false)
//    private String lastName;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    @Column(name = "phone_number")
//    private String phoneNumber;
//
//    @Column(name = "date_of_birth")
//    private LocalDate dateOfBirth;
//
//    private String gender;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "department_id")
//    private Department department;
//
//    private String position;
//
//    @Column(name = "join_date")
//    private LocalDate joinDate;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "employment_type")
//    private EmploymentType employmentType;
//
//    private BigDecimal salary;
//
//    @Column(name = "is_active")
//    private Boolean isActive = true;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Attendance> attendances;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PayrollRecord> payrollRecords;
//
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @Lob
//    @Column(name = "resume")
//    private byte[] resume;
//
//    @Lob
//    @Column(name = "id_proof")
//    private byte[] idProof;
//}


//package com.panoramic.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.panoramic.model.enums.EmploymentType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "employees")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "employee_id", unique = true, nullable = false)
//    private String employeeId;
//
//    @Column(name = "first_name", nullable = false)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false)
//    private String lastName;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    @Column(name = "phone_number")
//    private String phoneNumber;
//
//    @Column(name = "date_of_birth")
//    private LocalDate dateOfBirth;
//
//    private String gender;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "department_id")
//    @JsonBackReference
//    private Department department;
//
//    private String position;
//
//    @Column(name = "join_date")
//    private LocalDate joinDate;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "employment_type")
//    private EmploymentType employmentType;
//
//    private BigDecimal salary;
//
//    @Column(name = "is_active")
//    private Boolean isActive = true;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Attendance> attendances;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PayrollRecord> payrollRecords;
//
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    // --- Attributes for Resume File ---
//    @Column(name = "resume_path") // Stores the unique filename or relative path on the server
//    private String resumePath;
//
//    @Column(name = "resume_filename") // Stores the original filename for display/download
//    private String resumeFilename;
//
//    @Column(name = "resume_content_type") // Stores the MIME type (e.g., application/pdf)
//    private String resumeContentType;
//
//    @Column(name = "resume_size") // Stores the file size in bytes
//    private Long resumeSize;
//
//    // --- Attributes for ID Proof File ---
//    @Column(name = "id_proof_path") // Stores the unique filename or relative path on the server
//    private String idProofPath;
//
//    @Column(name = "id_proof_filename") // Stores the original filename for display/download
//    private String idProofFilename;
//
//    @Column(name = "id_proof_content_type") // Stores the MIME type (e.g., image/jpeg, application/pdf)
//    private String idProofContentType;
//
//    @Column(name = "id_proof_size") // Stores the file size in bytes
//    private Long idProofSize;
//}

package com.panoramic.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.panoramic.model.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference("department-employees")
    private Department department;

    private String position;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    private BigDecimal salary;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // FIXED: Add JsonIgnore to prevent circular reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // FIXED: Add JsonIgnore to prevent deep nesting
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PayrollRecord> payrollRecords;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // File attributes remain the same
    @Column(name = "resume_path")
    private String resumePath;

    @Column(name = "resume_filename")
    private String resumeFilename;

    @Column(name = "resume_content_type")
    private String resumeContentType;

    @Column(name = "resume_size")
    private Long resumeSize;

    @Column(name = "id_proof_path")
    private String idProofPath;

    @Column(name = "id_proof_filename")
    private String idProofFilename;

    @Column(name = "id_proof_content_type")
    private String idProofContentType;

    @Column(name = "id_proof_size")
    private Long idProofSize;
}
