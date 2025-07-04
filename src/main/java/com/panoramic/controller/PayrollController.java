package com.panoramic.controller;

import com.panoramic.dto.payroll.CreatePayrollRequestDto;
import com.panoramic.dto.payroll.PayrollRecordDto;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor

public class PayrollController {

    private final PayrollService payrollService;

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

    // ADD THIS NEW ENDPOINT
    @GetMapping("/{id}/payslip")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long id) {
        byte[] payslipPdf = payrollService.generatePayslipPdf(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=payslip-" + id + ".pdf")
                .body(payslipPdf);
    }
}
