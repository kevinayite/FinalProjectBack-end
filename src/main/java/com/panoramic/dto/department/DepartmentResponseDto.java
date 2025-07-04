package com.panoramic.dto.department;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentResponseDto {
    private Long id;
    private String name;
    private String description;
    private String departmentHead;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
