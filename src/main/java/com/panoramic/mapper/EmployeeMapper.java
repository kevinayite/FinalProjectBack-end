package com.panoramic.mapper;

import com.panoramic.dto.employee.EmployeeResponse;
import com.panoramic.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse dto = new EmployeeResponse();
        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setGender(employee.getGender());

        // Flatten department and user names
        dto.setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null);
        dto.setUserName(employee.getUser() != null ? employee.getUser().getUsername() : null);

        dto.setPosition(employee.getPosition());
        dto.setJoinDate(employee.getJoinDate());
        dto.setEmploymentType(employee.getEmploymentType());
        dto.setSalary(employee.getSalary());
        dto.setIsActive(employee.getIsActive());

        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        // Resume
        dto.setResumeOriginalFilename(employee.getResumeFilename());
        dto.setResumeContentType(employee.getResumeContentType());
        dto.setResumeSize(employee.getResumeSize());
        dto.setResumeDownloadUrl(
                employee.getResumePath() != null ? "/api/employees/" + employee.getId() + "/document/resume" : null
        );

        // ID Proof
        dto.setIdProofOriginalFilename(employee.getIdProofFilename());
        dto.setIdProofContentType(employee.getIdProofContentType());
        dto.setIdProofSize(employee.getIdProofSize());
        dto.setIdProofDownloadUrl(
                employee.getIdProofPath() != null ? "/api/employees/" + employee.getId() + "/document/idProof" : null
        );

        return dto;
    }
}
