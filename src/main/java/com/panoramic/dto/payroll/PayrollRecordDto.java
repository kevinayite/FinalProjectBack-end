package com.panoramic.dto.payroll;
import com.panoramic.model.enums.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRecordDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeId_display;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private BigDecimal salaryRate;
    private BigDecimal totalHoursWorked;
    private BigDecimal regularHours;
    private BigDecimal overtimeHours;
    private BigDecimal overtimeRateMultiplier;
    private Integer daysWorked;
    private Integer daysAbsent;
    private Integer daysLate;
    private BigDecimal basicPay;
    private BigDecimal overtimePay;
    private BigDecimal providentFund;
    private BigDecimal healthInsurance;
    private BigDecimal lateDeduction;
    private BigDecimal absentDeduction;
    private BigDecimal otherDeductions;
    private BigDecimal totalDeductions;
    private BigDecimal grossPay;
    private BigDecimal taxDeduction;
    private BigDecimal netPay;
    private PayrollStatus status;
    private LocalDate processedDate;
    private String processedBy;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
