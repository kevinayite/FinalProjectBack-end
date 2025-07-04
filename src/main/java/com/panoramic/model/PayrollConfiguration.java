//package com.panoramic.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "payroll_configuration")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public class PayrollConfiguration {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "config_name", unique = true, nullable = false)
//    private String configName = "DEFAULT";
//
//    @Column(name = "default_provident_fund_rate")
//    private BigDecimal defaultProvidentFundRate = new BigDecimal("0.12"); // 12%
//
//    @Column(name = "default_health_insurance")
//    private BigDecimal defaultHealthInsurance = new BigDecimal("500.00");
//
//    @Column(name = "overtime_rate_multiplier")
//    private BigDecimal overtimeRateMultiplier = new BigDecimal("1.5");
//
//    @Column(name = "late_penalty_per_occurrence")
//    private BigDecimal latePenaltyPerOccurrence = new BigDecimal("50.00");
//
//    @Column(name = "absent_penalty_per_day")
//    private BigDecimal absentPenaltyPerDay = new BigDecimal("200.00");
//
//    @Column(name = "standard_work_hours_per_day")
//    private BigDecimal standardWorkHoursPerDay = new BigDecimal("8.0");
//
//    @Column(name = "is_active")
//    private Boolean isActive = true;
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

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PayrollConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_name", unique = true, nullable = false)
    private String configName = "DEFAULT";

    @Column(name = "default_provident_fund_rate")
    private BigDecimal defaultProvidentFundRate = new BigDecimal("0.075"); // 12%

    @Column(name = "default_health_insurance")
    private BigDecimal defaultHealthInsurance = new BigDecimal("20.00");

    @Column(name = "overtime_rate_multiplier")
    private BigDecimal overtimeRateMultiplier = new BigDecimal("1.5");

    @Column(name = "late_penalty_per_occurrence")
    private BigDecimal latePenaltyPerOccurrence = new BigDecimal("5.00");

    @Column(name = "absent_penalty_per_day")
    private BigDecimal absentPenaltyPerDay = new BigDecimal("10.00");

    @Column(name = "standard_work_hours_per_day")
    private BigDecimal standardWorkHoursPerDay = new BigDecimal("8.0");

    // Employment type specific settings
    @Column(name = "full_time_monthly_hours")
    private BigDecimal fullTimeMonthlyHours = new BigDecimal("160.0"); // Standard 160 hours/month

    @Column(name = "intern_provident_fund_applicable")
    private Boolean internProvidentFundApplicable = false;

    @Column(name = "part_time_health_insurance_prorated")
    private Boolean partTimeHealthInsuranceProrated = true;

    @Column(name = "contract_health_insurance_applicable")
    private Boolean contractHealthInsuranceApplicable = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
