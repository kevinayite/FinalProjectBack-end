package com.panoramic.dto.attendance;

import com.panoramic.dto.department.DepartmentBasicDTO;
import com.panoramic.model.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAttendanceDTO {

    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private DepartmentBasicDTO department;
    private Boolean isActive;

    // Today's attendance info
    private TodayAttendanceDTO todayAttendance;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayAttendanceDTO {
        private Long id;
        private LocalDate attendanceDate;
        private LocalTime checkInTime;
        private LocalTime checkOutTime;
        private AttendanceStatus status;
        private Double hoursWorked;
        private String notes;
    }
}
