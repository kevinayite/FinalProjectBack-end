package com.panoramic.dto.department;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDashboardDto {
    private Long id;
    private String name;
    private Integer employeeCount;
    private Boolean isActive;
}
