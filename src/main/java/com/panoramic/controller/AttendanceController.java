package com.panoramic.controller;

import com.panoramic.dto.attendance.*;
import com.panoramic.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attendance Management", description = "APIs for managing employee attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Create attendance record", description = "Create a new attendance record for an employee")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseDTO> createAttendance(
            @Valid @RequestBody AttendanceRequestDTO requestDTO) {
        log.info("Creating attendance record for employee ID: {}", requestDTO.getEmployeeId());
        AttendanceResponseDTO response = attendanceService.createAttendance(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{attendanceId}")
    @Operation(summary = "Update attendance record", description = "Update an existing attendance record")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(
            @PathVariable Long attendanceId,
            @Valid @RequestBody AttendanceRequestDTO requestDTO) {
        log.info("Updating attendance record with ID: {}", attendanceId);
        AttendanceResponseDTO response = attendanceService.updateAttendance(attendanceId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{attendanceId}")
    @Operation(summary = "Get attendance by ID", description = "Retrieve attendance record by ID")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(@PathVariable Long attendanceId) {
        log.info("Fetching attendance record with ID: {}", attendanceId);
        AttendanceResponseDTO response = attendanceService.getAttendanceById(attendanceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/date/{date}")
    @Operation(summary = "Get attendance by employee and date", description = "Retrieve attendance record for specific employee and date")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and #employeeId == authentication.principal.employeeId)")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceByEmployeeAndDate(
            @PathVariable Long employeeId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching attendance for employee ID: {} on date: {}", employeeId, date);
        AttendanceResponseDTO response = attendanceService.getAttendanceByEmployeeAndDate(employeeId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/history")
    @Operation(summary = "Get employee attendance history", description = "Retrieve attendance history for an employee")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and #employeeId == authentication.principal.employeeId)")
    public ResponseEntity<List<AttendanceResponseDTO>> getEmployeeAttendanceHistory(
            @PathVariable Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Start date for filtering (optional)") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "End date for filtering (optional)") LocalDate endDate) {
        log.info("Fetching attendance history for employee ID: {}", employeeId);
        List<AttendanceResponseDTO> response = attendanceService.getEmployeeAttendanceHistory(employeeId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees")
    @Operation(summary = "Get all employees attendance", description = "Retrieve attendance records for all employees for a specific date")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeAttendanceListResponseDTO> getAllEmployeesAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date for attendance (defaults to today)") LocalDate date) {
        log.info("Fetching all employees attendance for date: {}", date);
        EmployeeAttendanceListResponseDTO response = attendanceService.getAllEmployeesAttendance(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get attendance statistics", description = "Get attendance statistics for a specific date")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<AttendanceStatsDTO> getAttendanceStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date for statistics (defaults to today)") LocalDate date) {
        log.info("Fetching attendance statistics for date: {}", date);
        AttendanceStatsDTO response = attendanceService.getAttendanceStats(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get attendance by date range", description = "Retrieve attendance records within a date range")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Start date", required = true) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "End date", required = true) LocalDate endDate) {
        log.info("Fetching attendance records from {} to {}", startDate, endDate);
        List<AttendanceResponseDTO> response = attendanceService.getAttendanceByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employee/{employeeId}/check-in")
    @Operation(summary = "Quick check-in", description = "Quick check-in for an employee")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and #employeeId == authentication.principal.employeeId)")
    public ResponseEntity<AttendanceResponseDTO> quickCheckIn(@PathVariable Long employeeId) {
        log.info("Processing quick check-in for employee ID: {}", employeeId);
        AttendanceResponseDTO response = attendanceService.quickCheckIn(employeeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employee/{employeeId}/check-out")
    @Operation(summary = "Quick check-out", description = "Quick check-out for an employee")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and #employeeId == authentication.principal.employeeId)")
    public ResponseEntity<AttendanceResponseDTO> quickCheckOut(@PathVariable Long employeeId) {
        log.info("Processing quick check-out for employee ID: {}", employeeId);
        AttendanceResponseDTO response = attendanceService.quickCheckOut(employeeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{attendanceId}")
    @Operation(summary = "Delete attendance record", description = "Delete an attendance record")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        log.info("Deleting attendance record with ID: {}", attendanceId);
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard-stats")
    @Operation(summary = "Get attendance dashboard statistics", description = "Get comprehensive attendance statistics for dashboard")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<AttendanceDashboardStatsDTO> getAttendanceDashboardStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date for statistics (defaults to today)") LocalDate date,
            @RequestParam(required = false, defaultValue = "7")
            @Parameter(description = "Number of days for trend data") int trendDays) {
        log.info("Fetching attendance dashboard statistics for date: {} with {} days trend", date, trendDays);
        AttendanceDashboardStatsDTO response = attendanceService.getAttendanceDashboardStats(date, trendDays);
        return ResponseEntity.ok(response);
    }

}
