package com.panoramic.controller;

import com.panoramic.dto.payroll.CreatePayrollRequestDto;
import com.panoramic.dto.payroll.PayrollRecordDto;
import com.panoramic.model.PayrollRecord;
import com.panoramic.model.enums.PayrollStatus;
import com.panoramic.repository.EmployeeRepository;
import com.panoramic.repository.PayrollRepository;
import com.panoramic.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor

public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;

    @PostMapping("/generate")
    public ResponseEntity<List<PayrollRecordDto>> generatePayrollRecords(
            @Valid @RequestBody CreatePayrollRequestDto request) {
        List<PayrollRecordDto> payrollRecords = payrollService.generatePayrollRecords(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payrollRecords);
    }

    @GetMapping
    public ResponseEntity<Page<PayrollRecordDto>> getAllPayrollRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PayrollRecordDto> payrollRecords = payrollService.getAllPayrollRecords(pageable);
        return ResponseEntity.ok(payrollRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollRecordDto> getPayrollRecordById(@PathVariable Long id) {
        PayrollRecordDto payrollRecord = payrollService.getPayrollRecordById(id);
        return ResponseEntity.ok(payrollRecord);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollRecordDto>> getPayrollRecordsByEmployee(@PathVariable Long employeeId) {
        List<PayrollRecordDto> payrollRecords = payrollService.getPayrollRecordsByEmployee(employeeId);
        return ResponseEntity.ok(payrollRecords);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PayrollRecordDto>> getPayrollRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PayrollRecordDto> payrollRecords = payrollService.getPayrollRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payrollRecords);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollRecordDto> updatePayrollRecord(
            @PathVariable Long id,
            @Valid @RequestBody PayrollRecordDto dto) {
        PayrollRecordDto updatedPayrollRecord = payrollService.updatePayrollRecord(id, dto);
        return ResponseEntity.ok(updatedPayrollRecord);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PayrollRecordDto> approvePayrollRecord(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String approvedBy = request.get("approvedBy");
        PayrollRecordDto approvedPayrollRecord = payrollService.approvePayrollRecord(id, approvedBy);
        return ResponseEntity.ok(approvedPayrollRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayrollRecord(@PathVariable Long id) {
        payrollService.deletePayrollRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPayrollStats() {
        Map<String, Object> stats = payrollService.getPayrollStats();
        return ResponseEntity.ok(stats);
    }


    @GetMapping("/{id}/payslip")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long id) {
        byte[] payslipPdf = payrollService.generatePayslipPdf(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=payslip-" + id + ".pdf")
                .body(payslipPdf);
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        LocalDate now = LocalDate.now();
        int currentMonth = month != null ? month : now.getMonthValue();
        int currentYear = year != null ? year : now.getYear();

        LocalDate startDate = LocalDate.of(currentYear, currentMonth, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Get payroll records for the month
        List<PayrollRecord> monthlyRecords = payrollRepository
                .findByPayPeriodStartBetween(startDate, endDate);

        // Calculate stats
        BigDecimal totalPayroll = monthlyRecords.stream()
                .map(PayrollRecord::getNetPay)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount = monthlyRecords.stream()
                .filter(r -> r.getStatus() == PayrollStatus.PENDING)
                .count();

        // Get average salary from employee records
        BigDecimal avgSalary = employeeRepository.getAverageSalary();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPayroll", totalPayroll);
        stats.put("pendingPayroll", pendingCount);
        stats.put("averageSalary", avgSalary);
        stats.put("payrollRecordCount", monthlyRecords.size());

        return ResponseEntity.ok(stats);
    }
}
