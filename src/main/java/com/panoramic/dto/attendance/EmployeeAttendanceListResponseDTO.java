package com.panoramic.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAttendanceListResponseDTO {

    private List<EmployeeAttendanceDTO> employees;
    private AttendanceStatsDTO stats;
    private int totalEmployees;
    private String date;
}
