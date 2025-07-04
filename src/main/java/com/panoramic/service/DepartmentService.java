package com.panoramic.service;




import com.panoramic.dto.department.DepartmentDashboardDto;
import com.panoramic.dto.department.DepartmentRequestDto;
import com.panoramic.dto.department.DepartmentResponseDto;
import com.panoramic.model.Department;
import com.panoramic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto) {
        Department department = new Department();
        BeanUtils.copyProperties(requestDto, department);
        departmentRepository.save(department);
        return convertToDto(department);
    }

    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return convertToDto(department);
    }

    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto requestDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        BeanUtils.copyProperties(requestDto, department, "id", "createdAt");
        departmentRepository.save(department);
        return convertToDto(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentResponseDto convertToDto(Department department) {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        BeanUtils.copyProperties(department, dto);
        return dto;
    }

    // Replace the getDepartmentDashboardStats method with this more efficient version
    public List<DepartmentDashboardDto> getDepartmentDashboardStats() {
        return departmentRepository.findDepartmentDashboardStats();
    }

}
