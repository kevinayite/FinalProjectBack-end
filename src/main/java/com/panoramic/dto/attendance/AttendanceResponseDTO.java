package com.panoramic.dto.attendance;

import com.panoramic.dto.employee.EmployeeBasicDTO;
import com.panoramic.model.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDTO {

    private Long id;
    private EmployeeBasicDTO employee;
    private LocalDate attendanceDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;
    private Double hoursWorked;
    private String notes;
    private LocalDateTime createdAt;
}
