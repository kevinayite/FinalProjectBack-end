package com.panoramic.dto.department;


import lombok.Data;

@Data
public class DepartmentRequestDto {
    private String name;
    private String description;
    private String departmentHead;
    private Boolean isActive;
}
