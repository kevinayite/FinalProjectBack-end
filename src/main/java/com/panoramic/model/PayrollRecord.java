//package com.panoramic.model;
//
//import com.panoramic.model.enums.PayrollStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "payroll_records")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public class PayrollRecord {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id", nullable = false)
//    private Employee employee;
//
//    @Column(name = "pay_period_start", nullable = false)
//    private LocalDate payPeriodStart;
//
//    @Column(name = "pay_period_end", nullable = false)
//    private LocalDate payPeriodEnd;
//
//    @Column(name = "basic_salary", nullable = false)
//    private BigDecimal basicSalary;
//
//    private BigDecimal allowances = BigDecimal.ZERO;
//
//    private BigDecimal deductions = BigDecimal.ZERO;
//
//    @Column(name = "gross_pay", nullable = false)
//    private BigDecimal grossPay;
//
//    @Column(name = "tax_deduction")
//    private BigDecimal taxDeduction = BigDecimal.ZERO;
//
//    @Column(name = "net_pay", nullable = false)
//    private BigDecimal netPay;
//
//    @Enumerated(EnumType.STRING)
//    private PayrollStatus status;
//
//    @Column(name = "processed_date")
//    private LocalDate processedDate;
//
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//}

package com.panoramic.model;

import com.panoramic.model.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PayrollRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "pay_period_start", nullable = false)
    private LocalDate payPeriodStart;

    @Column(name = "pay_period_end", nullable = false)
    private LocalDate payPeriodEnd;

    @Column(name = "salary_rate", nullable = false)
    private BigDecimal salaryRate;

    @Column(name = "total_hours_worked")
    private BigDecimal totalHoursWorked = BigDecimal.ZERO;

    @Column(name = "regular_hours")
    private BigDecimal regularHours = BigDecimal.ZERO;

    @Column(name = "overtime_hours")
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Column(name = "overtime_rate_multiplier")
    private BigDecimal overtimeRateMultiplier = new BigDecimal("1.5");

    @Column(name = "days_worked")
    private Integer daysWorked = 0;

    @Column(name = "days_absent")
    private Integer daysAbsent = 0;

    @Column(name = "days_late")
    private Integer daysLate = 0;

    //@Column(name = "basic_pay", nullable = false)
    @Column(name = "basic_salary", nullable = false)
    private BigDecimal basicPay = BigDecimal.ZERO;

    @Column(name = "overtime_pay")
    private BigDecimal overtimePay = BigDecimal.ZERO;

    @Column(name = "provident_fund")
    private BigDecimal providentFund = BigDecimal.ZERO;

    @Column(name = "health_insurance")
    private BigDecimal healthInsurance = BigDecimal.ZERO;

    @Column(name = "late_deduction")
    private BigDecimal lateDeduction = BigDecimal.ZERO;

    @Column(name = "absent_deduction")
    private BigDecimal absentDeduction = BigDecimal.ZERO;

    @Column(name = "other_deductions")
    private BigDecimal otherDeductions = BigDecimal.ZERO;

    @Column(name = "total_deductions")
    private BigDecimal totalDeductions = BigDecimal.ZERO;

    @Column(name = "gross_pay", nullable = false)
    private BigDecimal grossPay = BigDecimal.ZERO;

    @Column(name = "tax_deduction")
    private BigDecimal taxDeduction = BigDecimal.ZERO;

    @Column(name = "net_pay", nullable = false)
    private BigDecimal netPay = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    @Column(name = "processed_date")
    private LocalDate processedDate;

    @Column(name = "processed_by")
    private String processedBy;

    @Column(name = "remarks")
    private String remarks;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void calculatePayroll() {
        calculateTotalDeductions();
        calculateGrossPay();
        calculateNetPay();
    }

    private void calculateTotalDeductions() {
        this.totalDeductions = providentFund
                .add(healthInsurance)
                .add(lateDeduction)
                .add(absentDeduction)
                .add(otherDeductions);
    }

    private void calculateGrossPay() {
        this.grossPay = basicPay.add(overtimePay);
    }

    private void calculateNetPay() {
        this.netPay = grossPay
                .subtract(totalDeductions)
                .subtract(taxDeduction);
    }
}
