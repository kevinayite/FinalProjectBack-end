package com.panoramic.service;

import com.panoramic.dto.attendance.*;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.exception.BusinessException;
import com.panoramic.model.Attendance;
import com.panoramic.model.Employee;
import com.panoramic.model.enums.AttendanceStatus;
import com.panoramic.repository.AttendanceRepository;
import com.panoramic.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceResponseDTO createAttendance(AttendanceRequestDTO requestDTO) {
        log.info("Creating attendance record for employee ID: {}", requestDTO.getEmployeeId());

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));

        // Check if attendance already exists for this date
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, requestDTO.getAttendanceDate());

        if (existingAttendance.isPresent()) {
            throw new BusinessException("Attendance record already exists for this employee on " + requestDTO.getAttendanceDate());
        }

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setAttendanceDate(requestDTO.getAttendanceDate());
        attendance.setCheckInTime(requestDTO.getCheckInTime());
        attendance.setCheckOutTime(requestDTO.getCheckOutTime());
        attendance.setStatus(requestDTO.getStatus());
        attendance.setNotes(requestDTO.getNotes());

        // Calculate hours worked if both check-in and check-out times are provided
        if (requestDTO.getCheckInTime() != null && requestDTO.getCheckOutTime() != null) {
            double hoursWorked = calculateHoursWorked(requestDTO.getCheckInTime(), requestDTO.getCheckOutTime());
            attendance.setHoursWorked(hoursWorked);
        } else {
            attendance.setHoursWorked(requestDTO.getHoursWorked());
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        log.info("Attendance record created successfully with ID: {}", savedAttendance.getId());

        return convertToResponseDTO(savedAttendance);
    }

    public AttendanceResponseDTO updateAttendance(Long attendanceId, AttendanceRequestDTO requestDTO) {
        log.info("Updating attendance record with ID: {}", attendanceId);

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + attendanceId));

        // Verify employee exists
        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(requestDTO.getAttendanceDate());
        attendance.setCheckInTime(requestDTO.getCheckInTime());
        attendance.setCheckOutTime(requestDTO.getCheckOutTime());
        attendance.setStatus(requestDTO.getStatus());
        attendance.setNotes(requestDTO.getNotes());

        // Calculate hours worked if both times are provided
        if (requestDTO.getCheckInTime() != null && requestDTO.getCheckOutTime() != null) {
            double hoursWorked = calculateHoursWorked(requestDTO.getCheckInTime(), requestDTO.getCheckOutTime());
            attendance.setHoursWorked(hoursWorked);
        } else {
            attendance.setHoursWorked(requestDTO.getHoursWorked());
        }

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        log.info("Attendance record updated successfully");

        return convertToResponseDTO(updatedAttendance);
    }

    @Transactional(readOnly = true)
    public AttendanceResponseDTO getAttendanceById(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + attendanceId));

        return convertToResponseDTO(attendance);
    }

    @Transactional(readOnly = true)
    public AttendanceResponseDTO getAttendanceByEmployeeAndDate(Long employeeId, LocalDate date) {
        log.info("Fetching attendance for employee ID: {} on date: {}", employeeId, date);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Attendance attendance = attendanceRepository.findByEmployeeAndAttendanceDate(employee, date)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found for employee on " + date));

        return convertToResponseDTO(attendance);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getEmployeeAttendanceHistory(Long employeeId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching attendance history for employee ID: {} from {} to {}", employeeId, startDate, endDate);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        List<Attendance> attendanceList;
        if (startDate != null && endDate != null) {
            attendanceList = attendanceRepository.findByEmployeeAndDateRange(employee, startDate, endDate);
        } else {
            attendanceList = attendanceRepository.findByEmployeeOrderByAttendanceDateDesc(employee);
        }

        return attendanceList.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeAttendanceListResponseDTO getAllEmployeesAttendance(LocalDate date) {
        log.info("Fetching all employees attendance for date: {}", date);

        LocalDate targetDate = date != null ? date : LocalDate.now();

        List<Employee> allEmployees = employeeRepository.findByIsActiveTrue();
        List<Attendance> attendanceRecords = attendanceRepository.findByAttendanceDate(targetDate);

        // Create a map for quick lookup of attendance by employee ID
        Map<Long, Attendance> attendanceMap = attendanceRecords.stream()
                .collect(Collectors.toMap(
                        attendance -> attendance.getEmployee().getId(),
                        attendance -> attendance
                ));

        List<EmployeeAttendanceDTO> employeeAttendanceList = allEmployees.stream()
                .map(employee -> {
                    EmployeeAttendanceDTO dto = new EmployeeAttendanceDTO();
                    dto.setId(employee.getId());
                    dto.setEmployeeId(employee.getEmployeeId());
                    dto.setFirstName(employee.getFirstName());
                    dto.setLastName(employee.getLastName());
                    dto.setEmail(employee.getEmail());
                    dto.setPosition(employee.getPosition());
                    dto.setIsActive(employee.getIsActive());

                    if (employee.getDepartment() != null) {
                        dto.setDepartment(new com.panoramic.dto.department.DepartmentBasicDTO(
                                employee.getDepartment().getId(),
                                employee.getDepartment().getName()
                        ));
                    }

                    // Set today's attendance if exists
                    Attendance attendance = attendanceMap.get(employee.getId());
                    if (attendance != null) {
                        EmployeeAttendanceDTO.TodayAttendanceDTO todayAttendance =
                                new EmployeeAttendanceDTO.TodayAttendanceDTO();
                        todayAttendance.setId(attendance.getId());
                        todayAttendance.setAttendanceDate(attendance.getAttendanceDate());
                        todayAttendance.setCheckInTime(attendance.getCheckInTime());
                        todayAttendance.setCheckOutTime(attendance.getCheckOutTime());
                        todayAttendance.setStatus(attendance.getStatus());
                        todayAttendance.setHoursWorked(attendance.getHoursWorked());
                        todayAttendance.setNotes(attendance.getNotes());
                        dto.setTodayAttendance(todayAttendance);
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        // Calculate statistics
        AttendanceStatsDTO stats = calculateAttendanceStats(attendanceRecords, allEmployees.size());

        EmployeeAttendanceListResponseDTO response = new EmployeeAttendanceListResponseDTO();
        response.setEmployees(employeeAttendanceList);
        response.setStats(stats);
        response.setTotalEmployees(allEmployees.size());
        response.setDate(targetDate.toString());

        return response;
    }

    @Transactional(readOnly = true)
    public AttendanceStatsDTO getAttendanceStats(LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        List<Attendance> attendanceRecords = attendanceRepository.findByAttendanceDate(targetDate);
        int totalEmployees = (int) employeeRepository.countByIsActiveTrue();

        return calculateAttendanceStats(attendanceRecords, totalEmployees);
    }

    public void deleteAttendance(Long attendanceId) {
        log.info("Deleting attendance record with ID: {}", attendanceId);

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + attendanceId));

        attendanceRepository.delete(attendance);
        log.info("Attendance record deleted successfully");
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching attendance records from {} to {}", startDate, endDate);

        List<Attendance> attendanceList = attendanceRepository.findByDateRange(startDate, endDate);

        return attendanceList.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public AttendanceResponseDTO quickCheckIn(Long employeeId) {
        log.info("Processing quick check-in for employee ID: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Check if attendance already exists for today
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, today);

        if (existingAttendance.isPresent()) {
            Attendance attendance = existingAttendance.get();
            if (attendance.getCheckInTime() != null) {
                throw new BusinessException("Employee has already checked in today");
            }
            attendance.setCheckInTime(now);
            attendance.setStatus(AttendanceStatus.PRESENT);
            Attendance savedAttendance = attendanceRepository.save(attendance);
            return convertToResponseDTO(savedAttendance);
        } else {
            // Create new attendance record
            Attendance attendance = new Attendance();
            attendance.setEmployee(employee);
            attendance.setAttendanceDate(today);
            attendance.setCheckInTime(now);
            attendance.setStatus(AttendanceStatus.PRESENT);

            Attendance savedAttendance = attendanceRepository.save(attendance);
            return convertToResponseDTO(savedAttendance);
        }
    }

    public AttendanceResponseDTO quickCheckOut(Long employeeId) {
        log.info("Processing quick check-out for employee ID: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Attendance attendance = attendanceRepository.findByEmployeeAndAttendanceDate(employee, today)
                .orElseThrow(() -> new BusinessException("No check-in record found for today. Please check in first."));

        if (attendance.getCheckOutTime() != null) {
            throw new BusinessException("Employee has already checked out today");
        }

        if (attendance.getCheckInTime() == null) {
            throw new BusinessException("Cannot check out without checking in first");
        }

        attendance.setCheckOutTime(now);

        // Calculate hours worked
        double hoursWorked = calculateHoursWorked(attendance.getCheckInTime(), now);
        attendance.setHoursWorked(hoursWorked);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToResponseDTO(savedAttendance);
    }

    private double calculateHoursWorked(LocalTime checkIn, LocalTime checkOut) {
        if (checkIn == null || checkOut == null) {
            return 0.0;
        }

        long minutes = ChronoUnit.MINUTES.between(checkIn, checkOut);
        return Math.round((minutes / 60.0) * 100.0) / 100.0; // Round to 2 decimal places
    }

    private AttendanceStatsDTO calculateAttendanceStats(List<Attendance> attendanceRecords, int totalEmployees) {
        Map<AttendanceStatus, Long> statusCounts = attendanceRecords.stream()
                .collect(Collectors.groupingBy(Attendance::getStatus, Collectors.counting()));

        long present = statusCounts.getOrDefault(AttendanceStatus.PRESENT, 0L);
        long late = statusCounts.getOrDefault(AttendanceStatus.LATE, 0L);
        long halfDay = statusCounts.getOrDefault(AttendanceStatus.HALF_DAY, 0L);
        long sickLeave = statusCounts.getOrDefault(AttendanceStatus.SICK_LEAVE, 0L);
        long vacation = statusCounts.getOrDefault(AttendanceStatus.VACATION, 0L);
        long absent = statusCounts.getOrDefault(AttendanceStatus.ABSENT, 0L);

        // Calculate actual absent (employees without any record)
        long recordedAttendance = attendanceRecords.size();
        long actualAbsent = Math.max(0, totalEmployees - recordedAttendance) + absent;

        AttendanceStatsDTO stats = new AttendanceStatsDTO();
        stats.setPresent(present);
        stats.setAbsent(actualAbsent);
        stats.setLate(late);
        stats.setOnLeave(sickLeave + vacation);
        stats.setHalfDay(halfDay);
        stats.setTotal(totalEmployees);

        return stats;
    }

    private AttendanceResponseDTO convertToResponseDTO(Attendance attendance) {
        AttendanceResponseDTO dto = new AttendanceResponseDTO();
        dto.setId(attendance.getId());
        dto.setAttendanceDate(attendance.getAttendanceDate());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setStatus(attendance.getStatus());
        dto.setHoursWorked(attendance.getHoursWorked());
        dto.setNotes(attendance.getNotes());
        dto.setCreatedAt(attendance.getCreatedAt());

        // Set employee basic info
        Employee employee = attendance.getEmployee();
        com.panoramic.dto.employee.EmployeeBasicDTO employeeDTO =
                new com.panoramic.dto.employee.EmployeeBasicDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPosition(employee.getPosition());

        if (employee.getDepartment() != null) {
            employeeDTO.setDepartment(new com.panoramic.dto.department.DepartmentBasicDTO(
                    employee.getDepartment().getId(),
                    employee.getDepartment().getName()
            ));
        }

        dto.setEmployee(employeeDTO);

        return dto;
    }

    @Transactional(readOnly = true)
    public AttendanceDashboardStatsDTO getAttendanceDashboardStats(LocalDate date, int trendDays) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        LocalDate startDate = targetDate.minusDays(trendDays - 1);

        log.info("Calculating dashboard stats from {} to {}", startDate, targetDate);

        // Get today's stats
        AttendanceStatsDTO todayStats = getAttendanceStats(targetDate);

        // Get trend data
        List<AttendanceDashboardStatsDTO.DailyAttendanceTrendDTO> trendData =
                calculateTrendData(startDate, targetDate);

        // Calculate average attendance rate
        double averageAttendanceRate = trendData.stream()
                .mapToDouble(AttendanceDashboardStatsDTO.DailyAttendanceTrendDTO::getAttendanceRate)
                .average()
                .orElse(0.0);

        AttendanceDashboardStatsDTO dashboardStats = new AttendanceDashboardStatsDTO();
        dashboardStats.setTodayStats(todayStats);
        dashboardStats.setTrendData(trendData);
        dashboardStats.setAverageAttendanceRate(Math.round(averageAttendanceRate * 100.0) / 100.0);
        dashboardStats.setTotalWorkingDays(trendDays);
        dashboardStats.setDate(targetDate.toString());

        return dashboardStats;
    }

    private List<AttendanceDashboardStatsDTO.DailyAttendanceTrendDTO> calculateTrendData(
            LocalDate startDate, LocalDate endDate) {

        List<AttendanceDashboardStatsDTO.DailyAttendanceTrendDTO> trendData = new ArrayList<>();
        int totalEmployees = (int) employeeRepository.countByIsActiveTrue();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            List<Attendance> dayAttendance = attendanceRepository.findByAttendanceDate(currentDate);
            AttendanceStatsDTO dayStats = calculateAttendanceStats(dayAttendance, totalEmployees);

            double attendanceRate = totalEmployees > 0 ?
                    ((double) (dayStats.getPresent() + dayStats.getLate()) / totalEmployees) * 100 : 0.0;

            AttendanceDashboardStatsDTO.DailyAttendanceTrendDTO trendDTO =
                    new AttendanceDashboardStatsDTO.DailyAttendanceTrendDTO();
            trendDTO.setDate(currentDate.toString());
            trendDTO.setPresent(dayStats.getPresent());
            trendDTO.setAbsent(dayStats.getAbsent());
            trendDTO.setLate(dayStats.getLate());
            trendDTO.setOnLeave(dayStats.getOnLeave());
            trendDTO.setAttendanceRate(Math.round(attendanceRate * 100.0) / 100.0);

            trendData.add(trendDTO);
            currentDate = currentDate.plusDays(1);
        }

        return trendData;
    }

}
