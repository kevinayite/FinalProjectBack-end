package com.panoramic.service;


import com.itextpdf.io.source.ByteArrayOutputStream;
import com.panoramic.dto.payroll.CreatePayrollRequestDto;
import com.panoramic.dto.payroll.PayrollRecordDto;
import com.panoramic.model.*;
import com.panoramic.model.enums.AttendanceStatus;
import com.panoramic.model.enums.EmploymentType;
import com.panoramic.model.enums.PayrollStatus;
import com.panoramic.repository.AttendanceRepository;
import com.panoramic.repository.EmployeeRepository;
import com.panoramic.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollConfigurationService payrollConfigurationService;

    public List<PayrollRecordDto> generatePayrollRecords(CreatePayrollRequestDto request) {
        PayrollConfiguration config = payrollConfigurationService.getActiveConfigurationEntity();

        List<Employee> employees;
        if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {
            employees = employeeRepository.findAllById(request.getEmployeeIds());
        } else {
            employees = employeeRepository.findByIsActive(true);
        }

        return employees.stream()
                .map(employee -> generatePayrollForEmployee(employee, request, config))
                .collect(Collectors.toList());
    }

    public PayrollRecordDto generatePayrollForEmployee(Employee employee, CreatePayrollRequestDto request, PayrollConfiguration config) {
        // Check if payroll already exists for this period
        Optional<PayrollRecord> existingPayroll = payrollRepository
                .findByEmployeeAndPayPeriodStartAndPayPeriodEnd(
                        employee, request.getPayPeriodStart(), request.getPayPeriodEnd());

        if (existingPayroll.isPresent()) {
            throw new RuntimeException("Payroll already exists for employee " + employee.getEmployeeId() +
                    " for period " + request.getPayPeriodStart() + " to " + request.getPayPeriodEnd());
        }

        // Get attendance records for the period
        List<Attendance> attendanceRecords = attendanceRepository
                .findByEmployeeAndAttendanceDateBetween(employee, request.getPayPeriodStart(), request.getPayPeriodEnd());

        PayrollRecord payroll = new PayrollRecord();
        payroll.setEmployee(employee);
        payroll.setPayPeriodStart(request.getPayPeriodStart());
        payroll.setPayPeriodEnd(request.getPayPeriodEnd());
        payroll.setSalaryRate(employee.getSalary());
        payroll.setOvertimeRateMultiplier(config.getOvertimeRateMultiplier());
        payroll.setProcessedBy(request.getProcessedBy());
        payroll.setProcessedDate(LocalDate.now());
        payroll.setStatus(PayrollStatus.PENDING);

        // Calculate attendance statistics
        AttendanceStats stats = calculateAttendanceStats(attendanceRecords, config);

        payroll.setTotalHoursWorked(stats.totalHours);
        payroll.setRegularHours(stats.regularHours);
        payroll.setOvertimeHours(stats.overtimeHours);
        payroll.setDaysWorked(stats.daysWorked);
        payroll.setDaysAbsent(stats.daysAbsent);
        payroll.setDaysLate(stats.daysLate);

        // Calculate pay based on employment type
        calculateBasicPay(payroll, employee, config);
        calculateOvertimePay(payroll);
        calculateDeductions(payroll, config);

        payroll = payrollRepository.save(payroll);
        return convertToDto(payroll);
    }

    public Page<PayrollRecordDto> getAllPayrollRecords(Pageable pageable) {
        return payrollRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public PayrollRecordDto getPayrollRecordById(Long id) {
        PayrollRecord payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found with id: " + id));
        return convertToDto(payroll);
    }

    public List<PayrollRecordDto> getPayrollRecordsByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        return payrollRepository.findByEmployee(employee, Pageable.unpaged())
                .getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PayrollRecordDto> getPayrollRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return payrollRepository.findByPayPeriodStartBetween(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PayrollRecordDto updatePayrollRecord(Long id, PayrollRecordDto dto) {
        PayrollRecord existingPayroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found with id: " + id));

        updateEntityFromDto(existingPayroll, dto);
        existingPayroll = payrollRepository.save(existingPayroll);
        return convertToDto(existingPayroll);
    }

    public PayrollRecordDto approvePayrollRecord(Long id, String approvedBy) {
        PayrollRecord payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found with id: " + id));

        payroll.setStatus(PayrollStatus.APPROVED);
        payroll.setProcessedBy(approvedBy);
        payroll.setProcessedDate(LocalDate.now());

        payroll = payrollRepository.save(payroll);
        return convertToDto(payroll);
    }

    public void deletePayrollRecord(Long id) {
        PayrollRecord payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found with id: " + id));

        if (payroll.getStatus() == PayrollStatus.APPROVED) {
            throw new RuntimeException("Cannot delete approved payroll record");
        }

        payrollRepository.deleteById(id);
    }

    private AttendanceStats calculateAttendanceStats(List<Attendance> attendanceRecords, PayrollConfiguration config) {
        AttendanceStats stats = new AttendanceStats();
        BigDecimal standardHours = config.getStandardWorkHoursPerDay();

        for (Attendance attendance : attendanceRecords) {
            if (attendance.getStatus() == AttendanceStatus.PRESENT) {
                stats.daysWorked++;

                if (attendance.getHoursWorked() != null) {
                    BigDecimal hoursWorked = BigDecimal.valueOf(attendance.getHoursWorked());
                    stats.totalHours = stats.totalHours.add(hoursWorked);

                    if (hoursWorked.compareTo(standardHours) <= 0) {
                        stats.regularHours = stats.regularHours.add(hoursWorked);
                    } else {
                        stats.regularHours = stats.regularHours.add(standardHours);
                        stats.overtimeHours = stats.overtimeHours.add(hoursWorked.subtract(standardHours));
                    }
                }
            } else if (attendance.getStatus() == AttendanceStatus.ABSENT) {
                stats.daysAbsent++;
            } else if (attendance.getStatus() == AttendanceStatus.LATE) {
                stats.daysLate++;
                stats.daysWorked++;

                if (attendance.getHoursWorked() != null) {
                    BigDecimal hoursWorked = BigDecimal.valueOf(attendance.getHoursWorked());
                    stats.totalHours = stats.totalHours.add(hoursWorked);
                    stats.regularHours = stats.regularHours.add(hoursWorked);
                }
            }
        }

        return stats;
    }

    // Updated method to work with your EmploymentType enum
    private void calculateBasicPay(PayrollRecord payroll, Employee employee, PayrollConfiguration config) {
        BigDecimal salary = employee.getSalary();

        switch (employee.getEmploymentType()) {
            case FULL_TIME:
                // Full-time employees get full monthly salary regardless of hours
                payroll.setBasicPay(salary);
                break;

            case PART_TIME:
                // Part-time employees are paid based on hours worked
                // Assuming salary is stored as hourly rate for part-time employees
                BigDecimal hourlyRate = salary;
                BigDecimal basicPay = hourlyRate.multiply(payroll.getRegularHours());
                payroll.setBasicPay(basicPay);
                break;

            case CONTRACT:
                // Contract employees can be paid based on days worked or project basis
                // Here we'll use days worked approach
                // Assuming salary is stored as daily rate for contract employees
                BigDecimal dailyRate = salary;
                BigDecimal contractPay = dailyRate.multiply(new BigDecimal(payroll.getDaysWorked()));
                payroll.setBasicPay(contractPay);
                break;

            case INTERN:
                // Interns typically get a fixed stipend or hourly rate
                // Assuming salary is stored as monthly stipend for interns
                // But we can prorate based on days worked
                int totalWorkingDaysInMonth = calculateWorkingDaysInMonth(payroll.getPayPeriodStart(), payroll.getPayPeriodEnd());
                if (totalWorkingDaysInMonth > 0) {
                    BigDecimal dailyStipend = salary.divide(new BigDecimal(totalWorkingDaysInMonth), 2, RoundingMode.HALF_UP);
                    BigDecimal internPay = dailyStipend.multiply(new BigDecimal(payroll.getDaysWorked()));
                    payroll.setBasicPay(internPay);
                } else {
                    payroll.setBasicPay(salary); // Fallback to full amount
                }
                break;

            default:
                // Default case - treat as full-time
                payroll.setBasicPay(salary);
                break;
        }
    }

    private void calculateOvertimePay(PayrollRecord payroll) {
        if (payroll.getOvertimeHours().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal hourlyRate = calculateHourlyRate(payroll);
            BigDecimal overtimeRate = hourlyRate.multiply(payroll.getOvertimeRateMultiplier());
            BigDecimal overtimePay = overtimeRate.multiply(payroll.getOvertimeHours());
            payroll.setOvertimePay(overtimePay);
        }
    }

    private void calculateDeductions(PayrollRecord payroll, PayrollConfiguration config) {
        // Calculate provident fund (percentage of basic pay)
        // Note: Interns might not have provident fund deductions
        if (payroll.getEmployee().getEmploymentType() != EmploymentType.INTERN) {
            BigDecimal pfDeduction = payroll.getBasicPay().multiply(config.getDefaultProvidentFundRate());
            payroll.setProvidentFund(pfDeduction);
        }

        // Set health insurance (fixed amount)
        // Full-time employees get full health insurance, others might get prorated or none
        switch (payroll.getEmployee().getEmploymentType()) {
            case FULL_TIME:
                payroll.setHealthInsurance(config.getDefaultHealthInsurance());
                break;
            case PART_TIME:
            case CONTRACT:
                // Prorate health insurance based on days worked
                int totalDaysInMonth = payroll.getPayPeriodEnd().getDayOfMonth();
                BigDecimal proratedInsurance = config.getDefaultHealthInsurance()
                        .multiply(new BigDecimal(payroll.getDaysWorked()))
                        .divide(new BigDecimal(totalDaysInMonth), 2, RoundingMode.HALF_UP);
                payroll.setHealthInsurance(proratedInsurance);
                break;
            case INTERN:
                // Interns typically don't have health insurance deductions
                payroll.setHealthInsurance(BigDecimal.ZERO);
                break;
        }

        // Calculate late penalties
        BigDecimal lateDeduction = config.getLatePenaltyPerOccurrence()
                .multiply(new BigDecimal(payroll.getDaysLate()));
        payroll.setLateDeduction(lateDeduction);

        // Calculate absent penalties
        BigDecimal absentDeduction = config.getAbsentPenaltyPerDay()
                .multiply(new BigDecimal(payroll.getDaysAbsent()));
        payroll.setAbsentDeduction(absentDeduction);
    }

    private BigDecimal calculateHourlyRate(PayrollRecord payroll) {
        if (payroll.getRegularHours().compareTo(BigDecimal.ZERO) > 0) {
            return payroll.getBasicPay().divide(payroll.getRegularHours(), 2, RoundingMode.HALF_UP);
        }

        // Fallback calculation based on employment type
        switch (payroll.getEmployee().getEmploymentType()) {
            case FULL_TIME:
                // Assume 160 hours per month for full-time
                return payroll.getSalaryRate().divide(new BigDecimal("160"), 2, RoundingMode.HALF_UP);
            case PART_TIME:
                // For part-time, salary is already hourly rate
                return payroll.getSalaryRate();
            case CONTRACT:
                // Assume 8 hours per day for contract
                return payroll.getSalaryRate().divide(new BigDecimal("8"), 2, RoundingMode.HALF_UP);
            case INTERN:
                // Assume 160 hours per month for interns
                return payroll.getSalaryRate().divide(new BigDecimal("160"), 2, RoundingMode.HALF_UP);
            default:
                return payroll.getSalaryRate().divide(new BigDecimal("160"), 2, RoundingMode.HALF_UP);
        }
    }

    private int calculateWorkingDaysInMonth(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            // Exclude weekends (Saturday = 6, Sunday = 7)
            if (current.getDayOfWeek().getValue() < 6) {
                workingDays++;
            }
            current = current.plusDays(1);
        }

        return workingDays;
    }

    private PayrollRecordDto convertToDto(PayrollRecord entity) {
        PayrollRecordDto dto = new PayrollRecordDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setEmployeeName(entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName());
        dto.setEmployeeId_display(entity.getEmployee().getEmployeeId());
        dto.setPayPeriodStart(entity.getPayPeriodStart());
        dto.setPayPeriodEnd(entity.getPayPeriodEnd());
        dto.setSalaryRate(entity.getSalaryRate());
        dto.setTotalHoursWorked(entity.getTotalHoursWorked());
        dto.setRegularHours(entity.getRegularHours());
        dto.setOvertimeHours(entity.getOvertimeHours());
        dto.setOvertimeRateMultiplier(entity.getOvertimeRateMultiplier());
        dto.setDaysWorked(entity.getDaysWorked());
        dto.setDaysAbsent(entity.getDaysAbsent());
        dto.setDaysLate(entity.getDaysLate());
        dto.setBasicPay(entity.getBasicPay());
        dto.setOvertimePay(entity.getOvertimePay());
        dto.setProvidentFund(entity.getProvidentFund());
        dto.setHealthInsurance(entity.getHealthInsurance());
        dto.setLateDeduction(entity.getLateDeduction());
        dto.setAbsentDeduction(entity.getAbsentDeduction());
        dto.setOtherDeductions(entity.getOtherDeductions());
        dto.setTotalDeductions(entity.getTotalDeductions());
        dto.setGrossPay(entity.getGrossPay());
        dto.setTaxDeduction(entity.getTaxDeduction());
        dto.setNetPay(entity.getNetPay());
        dto.setStatus(entity.getStatus());
        dto.setProcessedDate(entity.getProcessedDate());
        dto.setProcessedBy(entity.getProcessedBy());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private void updateEntityFromDto(PayrollRecord entity, PayrollRecordDto dto) {
        entity.setOtherDeductions(dto.getOtherDeductions());
        entity.setTaxDeduction(dto.getTaxDeduction());
        entity.setRemarks(dto.getRemarks());
        entity.setStatus(dto.getStatus());
        if (dto.getProcessedBy() != null) {
            entity.setProcessedBy(dto.getProcessedBy());
        }
        if (dto.getProcessedDate() != null) {
            entity.setProcessedDate(dto.getProcessedDate());
        }
    }

    private static class AttendanceStats {
        BigDecimal totalHours = BigDecimal.ZERO;
        BigDecimal regularHours = BigDecimal.ZERO;
        BigDecimal overtimeHours = BigDecimal.ZERO;
        int daysWorked = 0;
        int daysAbsent = 0;
        int daysLate = 0;
    }

    public Map<String, Object> getPayrollStats() {
        Map<String, Object> stats = new HashMap<>();

        // Get all payroll records for current month/period
        List<PayrollRecord> allRecords = payrollRepository.findAll();

        // Calculate total payroll amount
        BigDecimal totalPayroll = allRecords.stream()
                .map(PayrollRecord::getNetPay)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Count unique employees
        long totalEmployees = allRecords.stream()
                .map(record -> record.getEmployee().getId())
                .distinct()
                .count();

        // Count pending approvals
        long pendingApproval = allRecords.stream()
                .filter(record -> record.getStatus() == PayrollStatus.PENDING)
                .count();

        // Count processing (draft + pending)
        long processing = allRecords.stream()
                .filter(record -> record.getStatus() == PayrollStatus.PENDING ||
                        record.getStatus() == PayrollStatus.DRAFT)
                .count();

        stats.put("totalPayroll", totalPayroll.doubleValue());
        stats.put("totalEmployees", totalEmployees);
        stats.put("pendingApproval", pendingApproval);
        stats.put("processing", processing);

        return stats;
    }

    // ADD THIS NEW METHOD
    public byte[] generatePayslipPdf(Long payrollId) {
        PayrollRecord payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll record not found with id: " + payrollId));

        // For now, return a simple PDF - you can enhance this later
        return createSimplePayslipPdf(payroll);
    }

//    private byte[] createSimplePayslipPdf(PayrollRecord payroll) {
//        try {
//            // Simple text-based PDF content
//            StringBuilder content = new StringBuilder();
//            content.append("PAYSLIP\n\n");
//            content.append("Employee: ").append(payroll.getEmployee().getFirstName())
//                    .append(" ").append(payroll.getEmployee().getLastName()).append("\n");
//            content.append("Employee ID: ").append(payroll.getEmployee().getEmployeeId()).append("\n");
//            content.append("Pay Period: ").append(payroll.getPayPeriodStart())
//                    .append(" to ").append(payroll.getPayPeriodEnd()).append("\n\n");
//            content.append("Basic Pay: $").append(payroll.getBasicPay()).append("\n");
//            content.append("Overtime Pay: $").append(payroll.getOvertimePay()).append("\n");
//            content.append("Gross Pay: $").append(payroll.getGrossPay()).append("\n");
//            content.append("Total Deductions: $").append(payroll.getTotalDeductions()).append("\n");
//            content.append("Net Pay: $").append(payroll.getNetPay()).append("\n");
//
//            // Convert to PDF bytes (simplified - you might want to use a proper PDF library)
//            return content.toString().getBytes();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating payslip PDF", e);
//        }
//    }

    private byte[] createSimplePayslipPdf(PayrollRecord payroll) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            document.add(new Paragraph("PAYSLIP")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());

            document.add(new Paragraph("\n"));

            // Employee information
            document.add(new Paragraph("Employee Information")
                    .setFontSize(14)
                    .setBold());

            Table empTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
            empTable.setWidth(UnitValue.createPercentValue(100));

            empTable.addCell("Employee Name:");
            empTable.addCell(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName());
            empTable.addCell("Employee ID:");
            empTable.addCell(payroll.getEmployee().getEmployeeId());
            empTable.addCell("Pay Period:");
            empTable.addCell(payroll.getPayPeriodStart() + " to " + payroll.getPayPeriodEnd());

            document.add(empTable);
            document.add(new Paragraph("\n"));

            // Payment details
            document.add(new Paragraph("Payment Details")
                    .setFontSize(14)
                    .setBold());

            Table payTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}));
            payTable.setWidth(UnitValue.createPercentValue(100));

            payTable.addCell("Basic Pay:");
            payTable.addCell("$" + payroll.getBasicPay().toString());
            payTable.addCell("Overtime Pay:");
            payTable.addCell("$" + payroll.getOvertimePay().toString());
            payTable.addCell("Gross Pay:");
            payTable.addCell("$" + payroll.getGrossPay().toString());
            payTable.addCell("Total Deductions:");
            payTable.addCell("$" + payroll.getTotalDeductions().toString());
            payTable.addCell("Net Pay:");
            payTable.addCell("$" + payroll.getNetPay().toString());

            document.add(payTable);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating payslip PDF", e);
        }
    }

}
