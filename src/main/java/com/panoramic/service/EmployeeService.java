//package com.panoramic.service;
//
//import com.panoramic.dto.employee.EmployeeRequest;
//import com.panoramic.dto.employee.EmployeeResponse;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.Department;
//import com.panoramic.model.Employee;
//import com.panoramic.model.User;
//import com.panoramic.repository.DepartmentRepository;
//import com.panoramic.repository.EmployeeRepository;
//import com.panoramic.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class EmployeeService {
//
//    private final EmployeeRepository employeeRepository;
//    private final DepartmentRepository departmentRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public EmployeeResponse createEmployee(EmployeeRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        // Check if employee ID already exists
//        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
//            throw new BadRequestException("Employee ID already exists");
//        }
//
//        // Check if email already exists
//        if (employeeRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        // Get department
//        Department department = departmentRepository.findById(request.getDepartmentId())
//                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
//
//        Employee employee = new Employee();
//        employee.setEmployeeId(request.getEmployeeId());
//        employee.setFirstName(request.getFirstName());
//        employee.setLastName(request.getLastName());
//        employee.setEmail(request.getEmail());
//        employee.setPhoneNumber(request.getPhoneNumber());
//        employee.setDateOfBirth(request.getDateOfBirth());
//        employee.setGender(request.getGender());
//        employee.setDepartment(department);
//        employee.setPosition(request.getPosition());
//        employee.setJoinDate(request.getJoinDate());
//        employee.setEmploymentType(request.getEmploymentType());
//        employee.setSalary(request.getSalary());
//        employee.setUser(user);
//        employee.setIsActive(true);
//
////        employee.setResume(request.getResume().getBytes());
////        employee.setIdProof(request.getIdProof().getBytes());
//        try {
//            employee.setResume(request.getResume().getBytes());
//            employee.setIdProof(request.getIdProof().getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to process uploaded files", e);
//        }
//
//
//        Employee savedEmployee = employeeRepository.save(employee);
//        return mapToResponse(savedEmployee);
//    }
//
//    public Page<EmployeeResponse> getEmployees(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        Page<Employee> employees = employeeRepository.findByUserId(user.getId(), pageable);
//        return employees.map(this::mapToResponse);
//    }
//
//    public EmployeeResponse getEmployeeById(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        // Check if employee belongs to current user
//        if (!employee.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Employee not found");
//        }
//
//        return mapToResponse(employee);
//    }
//
//    @Transactional
//    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        // Check if employee belongs to current user
//        if (!employee.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Employee not found");
//        }
//
//        // Check if employee ID already exists (excluding current employee)
//        if (!employee.getEmployeeId().equals(request.getEmployeeId()) &&
//            employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
//            throw new BadRequestException("Employee ID already exists");
//        }
//
//        // Check if email already exists (excluding current employee)
//        if (!employee.getEmail().equals(request.getEmail()) &&
//            employeeRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        // Get department
//        Department department = departmentRepository.findById(request.getDepartmentId())
//                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
//
//        employee.setEmployeeId(request.getEmployeeId());
//        employee.setFirstName(request.getFirstName());
//        employee.setLastName(request.getLastName());
//        employee.setEmail(request.getEmail());
//        employee.setPhoneNumber(request.getPhoneNumber());
//        employee.setDateOfBirth(request.getDateOfBirth());
//        employee.setGender(request.getGender());
//        employee.setDepartment(department);
//        employee.setPosition(request.getPosition());
//        employee.setJoinDate(request.getJoinDate());
//        employee.setEmploymentType(request.getEmploymentType());
//        employee.setSalary(request.getSalary());
//
//        Employee savedEmployee = employeeRepository.save(employee);
//        return mapToResponse(savedEmployee);
//    }
//
//    @Transactional
//    public void deleteEmployee(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        // Check if employee belongs to current user
//        if (!employee.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Employee not found");
//        }
//
//        employee.setIsActive(false);
//        employeeRepository.save(employee);
//    }
//
//    public Page<EmployeeResponse> searchEmployees(String search, Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        Page<Employee> employees = employeeRepository.findByUserIdAndSearch(user.getId(), search, pageable);
//        return employees.map(this::mapToResponse);
//    }
//
//    private User getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//
//    private EmployeeResponse mapToResponse(Employee employee) {
//        EmployeeResponse response = new EmployeeResponse();
//        response.setId(employee.getId());
//        response.setEmployeeId(employee.getEmployeeId());
//        response.setFirstName(employee.getFirstName());
//        response.setLastName(employee.getLastName());
//        response.setEmail(employee.getEmail());
//        response.setPhoneNumber(employee.getPhoneNumber());
//        response.setDateOfBirth(employee.getDateOfBirth());
//        response.setGender(employee.getGender());
//        response.setDepartmentName(employee.getDepartment().getName());
//        response.setPosition(employee.getPosition());
//        response.setJoinDate(employee.getJoinDate());
//        response.setEmploymentType(employee.getEmploymentType());
//        response.setSalary(employee.getSalary());
//        response.setIsActive(employee.getIsActive());
//        response.setCreatedAt(employee.getCreatedAt());
//        response.setUpdatedAt(employee.getUpdatedAt());
//        return response;
//    }
//}

//
//package com.panoramic.service;
//
//import com.panoramic.dto.employee.EmployeeRequest;
//import com.panoramic.dto.employee.EmployeeResponse;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.Department;
//import com.panoramic.model.Employee;
//import com.panoramic.model.User;
//import com.panoramic.repository.DepartmentRepository;
//import com.panoramic.repository.EmployeeRepository;
//import com.panoramic.repository.UserRepository;
//import jakarta.annotation.PostConstruct; // Import for @PostConstruct
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value; // Import for @Value
//import org.springframework.core.io.Resource; // Import for Spring's Resource
//import org.springframework.core.io.UrlResource; // Import for UrlResource
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile; // Import for MultipartFile
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // For generating download URLs
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files; // For file operations
//import java.nio.file.Path; // For path operations
//import java.nio.file.Paths; // For creating paths
//import java.nio.file.StandardCopyOption; // For file copy options
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID; // For generating unique filenames
//
//@Service
//@RequiredArgsConstructor
//public class EmployeeService {
//
//    private final EmployeeRepository employeeRepository;
//    private final DepartmentRepository departmentRepository;
//    private final UserRepository userRepository;
//
//    // Inject the file upload directory from application.properties
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    private Path fileStorageLocation;
//
//    /**
//     * Initializes the file storage directory. This method runs after the service has been constructed.
//     */
//    @PostConstruct
//    public void init() {
//        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
//        try {
//            Files.createDirectories(this.fileStorageLocation); // Create the directory if it doesn't exist
//            System.out.println("File upload directory created at: " + this.fileStorageLocation.toString());
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not create the file upload directory!", ex);
//        }
//    }
//
//    @Transactional
//    public EmployeeResponse createEmployee(
//            EmployeeRequest request,
//            MultipartFile resumeFile, // Add MultipartFile for resume
//            MultipartFile idProofFile, // Add MultipartFile for ID proof
//            Authentication authentication
//    ) {
//        User user = getCurrentUser(authentication);
//
//        // Check if employee ID already exists
//        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
//            throw new BadRequestException("Employee ID already exists");
//        }
//
//        // Check if email already exists
//        if (employeeRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        // Get department
//        Department department = departmentRepository.findById(request.getDepartmentId())
//                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
//
//        Employee employee = new Employee();
//        employee.setEmployeeId(request.getEmployeeId());
//        employee.setFirstName(request.getFirstName());
//        employee.setLastName(request.getLastName());
//        employee.setEmail(request.getEmail());
//        employee.setPhoneNumber(request.getPhoneNumber());
//        employee.setDateOfBirth(request.getDateOfBirth());
//        employee.setGender(request.getGender());
//        employee.setDepartment(department);
//        employee.setPosition(request.getPosition());
//        employee.setJoinDate(request.getJoinDate());
//        employee.setEmploymentType(request.getEmploymentType());
//        employee.setSalary(request.getSalary());
//        employee.setUser(user);
//        employee.setIsActive(true);
//
//        // --- Handle Resume File Upload ---
//        if (resumeFile != null && !resumeFile.isEmpty()) {
//            try {
//                String uniqueResumeFilename = storeFile(resumeFile);
//                employee.setResumePath(uniqueResumeFilename);
//                employee.setResumeFilename(resumeFile.getOriginalFilename());
//                employee.setResumeContentType(resumeFile.getContentType());
//                employee.setResumeSize(resumeFile.getSize());
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to store resume file: " + e.getMessage(), e);
//            }
//        }
//
//        // --- Handle ID Proof File Upload ---
//        if (idProofFile != null && !idProofFile.isEmpty()) {
//            try {
//                String uniqueIdProofFilename = storeFile(idProofFile);
//                employee.setIdProofPath(uniqueIdProofFilename);
//                employee.setIdProofFilename(idProofFile.getOriginalFilename());
//                employee.setIdProofContentType(idProofFile.getContentType());
//                employee.setIdProofSize(idProofFile.getSize());
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to store ID Proof file: " + e.getMessage(), e);
//            }
//        }
//
//        Employee savedEmployee = employeeRepository.save(employee);
//        return mapToResponse(savedEmployee);
//    }
//
//    public Page<EmployeeResponse> getEmployees(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        Page<Employee> employees = employeeRepository.findByUserId(user.getId(), pageable);
//        return employees.map(this::mapToResponse);
//    }
//
//    public EmployeeResponse getEmployeeById(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        // Check if employee belongs to current user
//        if (!employee.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Employee not found");
//        }
//
//        return mapToResponse(employee);
//    }
//
//    @Transactional
//    public EmployeeResponse updateEmployee(
//            Long id,
//            EmployeeRequest request,
//            MultipartFile resumeFile, // Add MultipartFile for resume update
//            MultipartFile idProofFile, // Add MultipartFile for ID proof update
//            Authentication authentication
//    ) {
//        User user = getCurrentUser(authentication);
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        // Check if employee belongs to current user
//        if (!employee.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Employee not found");
//        }
//
//        // Check if employee ID already exists (excluding current employee)
//        if (!employee.getEmployeeId().equals(request.getEmployeeId()) &&
//                employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
//            throw new BadRequestException("Employee ID already exists");
//        }
//
//        // Check if email already exists (excluding current employee)
//        if (!employee.getEmail().equals(request.getEmail()) &&
//                employeeRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        // Get department
//        Department department = departmentRepository.findById(request.getDepartmentId())
//                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
//
//        employee.setEmployeeId(request.getEmployeeId());
//        employee.setFirstName(request.getFirstName());
//        employee.setLastName(request.getLastName());
//        employee.setEmail(request.getEmail());
//        employee.setPhoneNumber(request.getPhoneNumber());
//        employee.setDateOfBirth(request.getDateOfBirth());
//        employee.setGender(request.getGender());
//        employee.setDepartment(department);
//        employee.setPosition(request.getPosition());
//        employee.setJoinDate(request.getJoinDate());
//        employee.setEmploymentType(request.getEmploymentType());
//        employee.setSalary(request.getSalary());
//
//        // --- Handle Resume File Update ---
//        if (resumeFile != null && !resumeFile.isEmpty()) {
//            try {
//                // Delete old resume file if exists
//                if (employee.getResumePath() != null) {
//                    deleteFile(employee.getResumePath());
//                }
//                String uniqueResumeFilename = storeFile(resumeFile);
//                employee.setResumePath(uniqueResumeFilename);
//                employee.setResumeFilename(resumeFile.getOriginalFilename());
//                employee.setResumeContentType(resumeFile.getContentType());
//                employee.setResumeSize(resumeFile.getSize());
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to update resume file: " + e.getMessage(), e);
//            }
//        }
//        // Consider logic for if resumeFile is null but there was an existing file
//        // e.g., if a checkbox on the frontend says "remove resume"
//
//        // --- Handle ID Proof File Update ---
//        if (idProofFile != null && !idProofFile.isEmpty()) {
//            try {
//                // Delete old ID proof file if exists
//                if (employee.getIdProofPath() != null) {
//                    deleteFile(employee.getIdProofPath());
//                }
//                String uniqueIdProofFilename = storeFile(idProofFile);
//                employee.setIdProofPath(uniqueIdProofFilename);
//                employee.setIdProofFilename(idProofFile.getOriginalFilename());
//                employee.setIdProofContentType(idProofFile.getContentType());
//                employee.setIdProofSize(idProofFile.getSize());
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to update ID Proof file: " + e.getMessage(), e);
//            }
//        }
//        // Consider logic for if idProofFile is null but there was an existing file
//
//        Employee savedEmployee = employeeRepository.save(employee);
//        return mapToResponse(savedEmployee);
//    }
//
//    @Transactional
//    public void deleteEmployee(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        // Check if employee belongs to current user
//        if (!employee.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Employee not found");
//        }
//
//        // Before setting isActive to false, delete associated files from file system
//        if (employee.getResumePath() != null) {
//            deleteFile(employee.getResumePath());
//        }
//        if (employee.getIdProofPath() != null) {
//            deleteFile(employee.getIdProofPath());
//        }
//
//        employee.setIsActive(false); // Soft delete
//        employeeRepository.save(employee); // Save the updated status
//        // If you're doing a hard delete (employeeRepository.delete(employee)), the cascade
//        // would handle related entities, but file deletion needs manual trigger.
//    }
//
//    public Page<EmployeeResponse> searchEmployees(String search, Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        // Ensure this method exists in your EmployeeRepository and properly filters by user ID and search term
//        Page<Employee> employees = employeeRepository.findByUserIdAndSearch(user.getId(), search, pageable);
//        return employees.map(this::mapToResponse);
//    }
//
//    /**
//     * Helper method to retrieve a file as a Spring Resource.
//     * Used by the Controller to serve files for download.
//     * @param employeeId The ID of the employee
//     * @param documentType "resume" or "idProof"
//     * @return Resource representing the file, or null if not found
//     */
//    public Resource loadDocumentAsResource(Long employeeId, String documentType) {
//        try {
//            Employee employee = employeeRepository.findById(employeeId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
//
//            String fileName = null;
//            if ("resume".equalsIgnoreCase(documentType)) {
//                fileName = employee.getResumePath();
//            } else if ("idProof".equalsIgnoreCase(documentType)) {
//                fileName = employee.getIdProofPath();
//            } else {
//                throw new BadRequestException("Invalid document type specified: " + documentType);
//            }
//
//            if (fileName == null || fileName.isBlank()) {
//                throw new ResourceNotFoundException(documentType + " not found for employee id " + employeeId);
//            }
//
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new ResourceNotFoundException("Could not read file: " + fileName);
//            }
//        } catch (MalformedURLException ex) {
//            throw new ResourceNotFoundException("File not found due to URL error: " + ex.getMessage(), ex);
//        }
//    }
//
//    /**
//     * Private helper method to store a MultipartFile to the file system.
//     * @param file The MultipartFile to store.
//     * @return The unique filename generated for the stored file.
//     * @throws IOException If file storage fails.
//     */
//    private String storeFile(MultipartFile file) throws IOException {
//        String originalFilename = file.getOriginalFilename();
//        String fileExtension = "";
//        int dotIndex = originalFilename != null ? originalFilename.lastIndexOf('.') : -1;
//        if (dotIndex > 0) {
//            fileExtension = originalFilename.substring(dotIndex);
//        }
//
//        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
//        Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
//
//        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//        return uniqueFilename;
//    }
//
//    /**
//     * Private helper method to delete a file from the file system.
//     * @param uniqueFilename The unique filename of the file to delete.
//     */
//    private void deleteFile(String uniqueFilename) {
//        if (uniqueFilename != null && !uniqueFilename.isBlank()) {
//            Path filePath = this.fileStorageLocation.resolve(uniqueFilename).normalize();
//            try {
//                Files.deleteIfExists(filePath);
//                System.out.println("Deleted file: " + filePath);
//            } catch (IOException e) {
//                System.err.println("Could not delete file: " + filePath + " - " + e.getMessage());
//                // Consider logging this or rethrowing as a custom exception if critical
//            }
//        }
//    }
//
//    private User getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//
//    private EmployeeResponse mapToResponse(Employee employee) {
//        EmployeeResponse response = new EmployeeResponse();
//        response.setId(employee.getId());
//        response.setEmployeeId(employee.getEmployeeId());
//        response.setFirstName(employee.getFirstName());
//        response.setLastName(employee.getLastName());
//        response.setEmail(employee.getEmail());
//        response.setPhoneNumber(employee.getPhoneNumber());
//        response.setDateOfBirth(employee.getDateOfBirth());
//        response.setGender(employee.getGender());
//        // Handle department and user might be null if not eagerly loaded or relationships are broken
//        response.setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null);
//        response.setPosition(employee.getPosition());
//        response.setJoinDate(employee.getJoinDate());
//        response.setEmploymentType(employee.getEmploymentType());
//        response.setSalary(employee.getSalary());
//        response.setIsActive(employee.getIsActive());
//        response.setCreatedAt(employee.getCreatedAt());
//        response.setUpdatedAt(employee.getUpdatedAt());
//        response.setUserName(employee.getUser() != null ? employee.getUser().getUsername() : null);
//
//        // --- Map Resume File Data to Response ---
//        if (employee.getResumePath() != null) {
//            response.setResumeOriginalFilename(employee.getResumeFilename());
//            response.setResumeContentType(employee.getResumeContentType());
//            response.setResumeSize(employee.getResumeSize());
//            // Construct the download URL for the resume
//            response.setResumeDownloadUrl(
//                    ServletUriComponentsBuilder.fromCurrentContextPath()
//                            .path("/api/employees/") // Base path for employees
//                            .path(employee.getId().toString()) // Employee ID
//                            .path("/document/resume") // Specific document endpoint for resume
//                            .toUriString()
//            );
//        }
//
//        // --- Map ID Proof File Data to Response ---
//        if (employee.getIdProofPath() != null) {
//            response.setIdProofOriginalFilename(employee.getIdProofFilename());
//            response.setIdProofContentType(employee.getIdProofContentType());
//            response.setIdProofSize(employee.getIdProofSize());
//            // Construct the download URL for the ID proof
//            response.setIdProofDownloadUrl(
//                    ServletUriComponentsBuilder.fromCurrentContextPath()
//                            .path("/api/employees/") // Base path for employees
//                            .path(employee.getId().toString()) // Employee ID
//                            .path("/document/idProof") // Specific document endpoint for ID proof
//                            .toUriString()
//            );
//        }
//
//        return response;
//    }
//}

package com.panoramic.service;
import com.panoramic.dto.employee.EmployeePatchRequest;
import com.panoramic.dto.employee.EmployeeRequest;
import com.panoramic.dto.employee.EmployeeResponse;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.mapper.EmployeeMapper;
import com.panoramic.model.Department;
import com.panoramic.model.Employee;
import com.panoramic.model.User;
import com.panoramic.repository.DepartmentRepository;
import com.panoramic.repository.EmployeeRepository;
import com.panoramic.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path fileStorageLocation;

    private EmployeeResponse convertToDto(Employee employee) {
        EmployeeResponse dto = new EmployeeResponse();

        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setGender(employee.getGender());
        dto.setDepartmentName(
                employee.getDepartment() != null ? employee.getDepartment().getName() : null
        );
        dto.setPosition(employee.getPosition());
        dto.setJoinDate(employee.getJoinDate());
        dto.setEmploymentType(employee.getEmploymentType());
        dto.setSalary(employee.getSalary());
        dto.setIsActive(employee.getIsActive());
        dto.setUserName(
                employee.getUser() != null ? employee.getUser().getUsername() : null
        );
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        // Resume info
        dto.setResumeOriginalFilename(employee.getResumeFilename());
        dto.setResumeContentType(employee.getResumeContentType());
        dto.setResumeSize(employee.getResumeSize());
        dto.setResumeDownloadUrl(
                employee.getResumePath() != null
                        ? "/api/employees/" + employee.getId() + "/document/resume"
                        : null
        );

        // ID Proof info
        dto.setIdProofOriginalFilename(employee.getIdProofFilename());
        dto.setIdProofContentType(employee.getIdProofContentType());
        dto.setIdProofSize(employee.getIdProofSize());
        dto.setIdProofDownloadUrl(
                employee.getIdProofPath() != null
                        ? "/api/employees/" + employee.getId() + "/document/idProof"
                        : null
        );

        return dto;
    }



//    public Page<EmployeeResponse> getEmployeesByDepartment(Long departmentId, Authentication authentication, Pageable pageable) {
//        Page<Employee> employeePage = employeeRepository.findByDepartmentId(departmentId, pageable);
//        return employeePage.map(employeeMapper::toResponse);
//    }

    public Page<EmployeeResponse> getEmployeesByDepartment(Long departmentId, Authentication authentication, Pageable pageable) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));

        Page<Employee> employeePage = employeeRepository.findByDepartment(department, pageable);
        return employeePage.map(employeeMapper::toResponse);
    }



    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("File upload directory created at: " + this.fileStorageLocation.toString());
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the file upload directory!", ex);
        }
    }

    @Transactional
    public EmployeeResponse createEmployee(
            EmployeeRequest request,
            MultipartFile resumeFile,
            MultipartFile idProofFile,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);

        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new BadRequestException("Employee ID already exists");
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Employee employee = new Employee();
        employee.setEmployeeId(request.getEmployeeId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setDepartment(department);
        employee.setPosition(request.getPosition());
        employee.setJoinDate(request.getJoinDate());
        employee.setEmploymentType(request.getEmploymentType());
        employee.setSalary(request.getSalary());
        employee.setUser(user);
        employee.setIsActive(true);

        // --- Handle Resume File Upload ---
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                String uniqueResumeFilename = storeFile(resumeFile);
                employee.setResumePath(uniqueResumeFilename);
                employee.setResumeFilename(resumeFile.getOriginalFilename());
                employee.setResumeContentType(resumeFile.getContentType());
                employee.setResumeSize(resumeFile.getSize());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store resume file: " + e.getMessage(), e);
            }
        }

        // --- Handle ID Proof File Upload ---
        if (idProofFile != null && !idProofFile.isEmpty()) {
            try {
                String uniqueIdProofFilename = storeFile(idProofFile);
                employee.setIdProofPath(uniqueIdProofFilename);
                employee.setIdProofFilename(idProofFile.getOriginalFilename());
                employee.setIdProofContentType(idProofFile.getContentType());
                employee.setIdProofSize(idProofFile.getSize());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store ID Proof file: " + e.getMessage(), e);
            }
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    public Page<EmployeeResponse> getEmployees(Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        Page<Employee> employees = employeeRepository.findByUserIdWithDepartment(user.getId(), pageable);
        return employees.map(this::mapToResponse);
    }
public Page<EmployeeResponse> getEmployees(Authentication authentication, Pageable pageable, Boolean isActive) {
    Page<Employee> employees;

    if (isActive != null) {
        employees = employeeRepository.findByIsActive(isActive, pageable);
    } else {
        employees = employeeRepository.findAll(pageable);
    }

    return employees.map(this::convertToDto);
}



    public Employee getEmployeeEntityById(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Employee employee = employeeRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        // Check if employee belongs to current user
        if (!employee.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Employee not found or unauthorized access for id " + id);
        }
        return employee;
    }


    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id, Authentication authentication) {
        Employee employee = getEmployeeEntityById(id, authentication); // Use the new helper
        return mapToResponse(employee);
    }


    @Transactional
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request,
            MultipartFile resumeFile,
            MultipartFile idProofFile,
            Authentication authentication
    ) {
        Employee employee = getEmployeeEntityById(id, authentication); // Use the new helper for security check

        if (!employee.getEmployeeId().equals(request.getEmployeeId()) &&
                employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new BadRequestException("Employee ID already exists");
        }

        if (!employee.getEmail().equals(request.getEmail()) &&
                employeeRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        employee.setEmployeeId(request.getEmployeeId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setDepartment(department);
        employee.setPosition(request.getPosition());
        employee.setJoinDate(request.getJoinDate());
        employee.setEmploymentType(request.getEmploymentType());
        employee.setSalary(request.getSalary());

        // --- Handle Resume File Update ---
//        if (resumeFile != null && !resumeFile.isEmpty()) {
//            try {
//                if (employee.getResumePath() != null) {
//                    deleteFile(employee.getResumePath());
//                }
//                String uniqueResumeFilename = storeFile(resumeFile);
//                employee.setResumePath(uniqueResumeFilename);
//                employee.setResumeFilename(resumeFile.getOriginalFilename());
//                employee.setResumeContentType(resumeFile.getContentType());
//                employee.setResumeSize(resumeFile.getSize());
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to update resume file: " + e.getMessage(), e);
//            }
//        } else if (request.getResumeOriginalFilename() == null) { // Assuming null/empty originalFilename in request means remove
//            // If resumeFile is null AND frontend explicitly indicates removal (e.g., by sending null for filename)
//            // You might need a flag in EmployeeRequest to explicitly remove files if not uploading a new one.
//            // For now, if no file is uploaded, and there was an existing one, it remains.
//            // If you want to allow clearing files, add a `boolean clearResume` to EmployeeRequest.
//            if (employee.getResumePath() != null) {
//                deleteFile(employee.getResumePath());
//                employee.setResumePath(null);
//                employee.setResumeFilename(null);
//                employee.setResumeContentType(null);
//                employee.setResumeSize(null);
//            }
//        }
//
//
//        // --- Handle ID Proof File Update ---
//        if (idProofFile != null && !idProofFile.isEmpty()) {
//            try {
//                if (employee.getIdProofPath() != null) {
//                    deleteFile(employee.getIdProofPath());
//                }
//                String uniqueIdProofFilename = storeFile(idProofFile);
//                employee.setIdProofPath(uniqueIdProofFilename);
//                employee.setIdProofFilename(idProofFile.getOriginalFilename());
//                employee.setIdProofContentType(idProofFile.getContentType());
//                employee.setIdProofSize(idProofFile.getSize());
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to update ID Proof file: " + e.getMessage(), e);
//            }
//        } else if (request.getIdProofOriginalFilename() == null) { // Assuming null/empty originalFilename in request means remove
//            if (employee.getIdProofPath() != null) {
//                deleteFile(employee.getIdProofPath());
//                employee.setIdProofPath(null);
//                employee.setIdProofFilename(null);
//                employee.setIdProofContentType(null);
//                employee.setIdProofSize(null);
//            }
//        }
        // --- Handle Resume File Update ---
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                // Delete old file if exists
                if (employee.getResumePath() != null) {
                    deleteFile(employee.getResumePath());
                }
                String uniqueResumeFilename = storeFile(resumeFile);
                employee.setResumePath(uniqueResumeFilename);
                employee.setResumeFilename(resumeFile.getOriginalFilename());
                employee.setResumeContentType(resumeFile.getContentType());
                employee.setResumeSize(resumeFile.getSize());
            } catch (IOException e) {
                throw new RuntimeException("Failed to update resume file: " + e.getMessage(), e);
            }
        } else if (Boolean.TRUE.equals(request.getClearResume())) {
            // Explicitly clear the resume file
            if (employee.getResumePath() != null) {
                deleteFile(employee.getResumePath());
                employee.setResumePath(null);
                employee.setResumeFilename(null);
                employee.setResumeContentType(null);
                employee.setResumeSize(null);
            }
        }
// If neither new file nor clear flag, keep existing file unchanged

// --- Handle ID Proof File Update (similar logic) ---
        if (idProofFile != null && !idProofFile.isEmpty()) {
            try {
                if (employee.getIdProofPath() != null) {
                    deleteFile(employee.getIdProofPath());
                }
                String uniqueIdProofFilename = storeFile(idProofFile);
                employee.setIdProofPath(uniqueIdProofFilename);
                employee.setIdProofFilename(idProofFile.getOriginalFilename());
                employee.setIdProofContentType(idProofFile.getContentType());
                employee.setIdProofSize(idProofFile.getSize());
            } catch (IOException e) {
                throw new RuntimeException("Failed to update ID Proof file: " + e.getMessage(), e);
            }
        } else if (Boolean.TRUE.equals(request.getClearIdProof())) {
            if (employee.getIdProofPath() != null) {
                deleteFile(employee.getIdProofPath());
                employee.setIdProofPath(null);
                employee.setIdProofFilename(null);
                employee.setIdProofContentType(null);
                employee.setIdProofSize(null);
            }
        }


        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id, Authentication authentication) {
        Employee employee = getEmployeeEntityById(id, authentication); // Use the new helper

        // Before setting isActive to false, delete associated files from file system
        if (employee.getResumePath() != null) {
            deleteFile(employee.getResumePath());
        }
        if (employee.getIdProofPath() != null) {
            deleteFile(employee.getIdProofPath());
        }

        employee.setIsActive(false);
        employeeRepository.save(employee);
    }

    public Page<EmployeeResponse> searchEmployees(String search, Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        Page<Employee> employees = employeeRepository.findByUserIdAndSearch(user.getId(), search, pageable);
        return employees.map(this::mapToResponse);
    }

    public Resource loadFileAsResource(String uniqueFilename) {
        try {
            if (uniqueFilename == null || uniqueFilename.isBlank()) {
                throw new ResourceNotFoundException("File name cannot be null or blank.");
            }
            Path filePath = this.fileStorageLocation.resolve(uniqueFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Could not read file: " + uniqueFilename);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File access error for " + uniqueFilename + ": " + ex.getMessage(), ex);
        }
    }


    private String storeFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename != null ? originalFilename.lastIndexOf('.') : -1;
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private void deleteFile(String uniqueFilename) {
        if (uniqueFilename != null && !uniqueFilename.isBlank()) {
            Path filePath = this.fileStorageLocation.resolve(uniqueFilename).normalize();
            try {
                Files.deleteIfExists(filePath);
                System.out.println("Deleted file: " + filePath);
            } catch (IOException e) {
                System.err.println("Could not delete file: " + filePath + " - " + e.getMessage());
            }
        }
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setEmployeeId(employee.getEmployeeId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhoneNumber(employee.getPhoneNumber());
        response.setDateOfBirth(employee.getDateOfBirth());
        response.setGender(employee.getGender());
        response.setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null);
        response.setPosition(employee.getPosition());
        response.setJoinDate(employee.getJoinDate());
        response.setEmploymentType(employee.getEmploymentType());
        response.setSalary(employee.getSalary());
        response.setIsActive(employee.getIsActive());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());
        response.setUserName(employee.getUser() != null ? employee.getUser().getUsername() : null);

        if (employee.getResumePath() != null) {
            response.setResumeOriginalFilename(employee.getResumeFilename());
            response.setResumeContentType(employee.getResumeContentType());
            response.setResumeSize(employee.getResumeSize());
            response.setResumeDownloadUrl(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/employees/")
                            .path(employee.getId().toString())
                            .path("/document/resume")
                            .toUriString()
            );
        }

        if (employee.getIdProofPath() != null) {
            response.setIdProofOriginalFilename(employee.getIdProofFilename());
            response.setIdProofContentType(employee.getIdProofContentType());
            response.setIdProofSize(employee.getIdProofSize());
            response.setIdProofDownloadUrl(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/employees/")
                            .path(employee.getId().toString())
                            .path("/document/idProof")
                            .toUriString()
            );
        }

        return response;
    }

    @Transactional
    public EmployeeResponse patchEmployee(
            Long id,
            EmployeePatchRequest patchRequest,
            MultipartFile resumeFile,
            MultipartFile idProofFile,
            Authentication authentication
    ) {
        Employee employee = getEmployeeEntityById(id, authentication);

        // Only update fields that are provided (not null)
        if (patchRequest.getEmployeeId() != null) {
            if (!employee.getEmployeeId().equals(patchRequest.getEmployeeId()) &&
                    employeeRepository.existsByEmployeeId(patchRequest.getEmployeeId())) {
                throw new BadRequestException("Employee ID already exists");
            }
            employee.setEmployeeId(patchRequest.getEmployeeId());
        }

        if (patchRequest.getFirstName() != null) {
            employee.setFirstName(patchRequest.getFirstName());
        }

        if (patchRequest.getLastName() != null) {
            employee.setLastName(patchRequest.getLastName());
        }

        if (patchRequest.getEmail() != null) {
            if (!employee.getEmail().equals(patchRequest.getEmail()) &&
                    employeeRepository.existsByEmail(patchRequest.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
            employee.setEmail(patchRequest.getEmail());
        }

        if (patchRequest.getPhoneNumber() != null) {
            employee.setPhoneNumber(patchRequest.getPhoneNumber());
        }

        if (patchRequest.getDateOfBirth() != null) {
            employee.setDateOfBirth(patchRequest.getDateOfBirth());
        }

        if (patchRequest.getGender() != null) {
            employee.setGender(patchRequest.getGender());
        }

        if (patchRequest.getDepartmentId() != null) {
            Department department = departmentRepository.findById(patchRequest.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        if (patchRequest.getPosition() != null) {
            employee.setPosition(patchRequest.getPosition());
        }

        if (patchRequest.getJoinDate() != null) {
            employee.setJoinDate(patchRequest.getJoinDate());
        }

        if (patchRequest.getEmploymentType() != null) {
            employee.setEmploymentType(patchRequest.getEmploymentType());
        }

        if (patchRequest.getSalary() != null) {
            employee.setSalary(patchRequest.getSalary());
        }

        if (patchRequest.getIsActive() != null) {
            employee.setIsActive(patchRequest.getIsActive());
        }

        // Handle Resume File Update
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                if (employee.getResumePath() != null) {
                    deleteFile(employee.getResumePath());
                }
                String uniqueResumeFilename = storeFile(resumeFile);
                employee.setResumePath(uniqueResumeFilename);
                employee.setResumeFilename(resumeFile.getOriginalFilename());
                employee.setResumeContentType(resumeFile.getContentType());
                employee.setResumeSize(resumeFile.getSize());
            } catch (IOException e) {
                throw new RuntimeException("Failed to update resume file: " + e.getMessage(), e);
            }
        } else if (Boolean.TRUE.equals(patchRequest.getClearResume())) {
            if (employee.getResumePath() != null) {
                deleteFile(employee.getResumePath());
                employee.setResumePath(null);
                employee.setResumeFilename(null);
                employee.setResumeContentType(null);
                employee.setResumeSize(null);
            }
        }

        // Handle ID Proof File Update
        if (idProofFile != null && !idProofFile.isEmpty()) {
            try {
                if (employee.getIdProofPath() != null) {
                    deleteFile(employee.getIdProofPath());
                }
                String uniqueIdProofFilename = storeFile(idProofFile);
                employee.setIdProofPath(uniqueIdProofFilename);
                employee.setIdProofFilename(idProofFile.getOriginalFilename());
                employee.setIdProofContentType(idProofFile.getContentType());
                employee.setIdProofSize(idProofFile.getSize());
            } catch (IOException e) {
                throw new RuntimeException("Failed to update ID Proof file: " + e.getMessage(), e);
            }
        } else if (Boolean.TRUE.equals(patchRequest.getClearIdProof())) {
            if (employee.getIdProofPath() != null) {
                deleteFile(employee.getIdProofPath());
                employee.setIdProofPath(null);
                employee.setIdProofFilename(null);
                employee.setIdProofContentType(null);
                employee.setIdProofSize(null);
            }
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

}