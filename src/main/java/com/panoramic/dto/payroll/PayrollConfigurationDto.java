//package com.panoramic.dto.payroll;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import jakarta.validation.constraints.DecimalMin;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import java.math.BigDecimal;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PayrollConfigurationDto {
//    private Long id;
//
//    @NotBlank(message = "Configuration name is required")
//    private String configName;
//
//    @NotNull(message = "Provident fund rate is required")
//    @DecimalMin(value = "0.0", message = "Provident fund rate must be non-negative")
//    private BigDecimal defaultProvidentFundRate;
//
//    @NotNull(message = "Health insurance amount is required")
//    @DecimalMin(value = "0.0", message = "Health insurance amount must be non-negative")
//    private BigDecimal defaultHealthInsurance;
//
//    @NotNull(message = "Overtime rate multiplier is required")
//    @DecimalMin(value = "1.0", message = "Overtime rate multiplier must be at least 1.0")
//    private BigDecimal overtimeRateMultiplier;
//
//    @NotNull(message = "Late penalty is required")
//    @DecimalMin(value = "0.0", message = "Late penalty must be non-negative")
//    private BigDecimal latePenaltyPerOccurrence;
//
//    @NotNull(message = "Absent penalty is required")
//    @DecimalMin(value = "0.0", message = "Absent penalty must be non-negative")
//    private BigDecimal absentPenaltyPerDay;
//
//    @NotNull(message = "Standard work hours is required")
//    @DecimalMin(value = "1.0", message = "Standard work hours must be at least 1.0")
//    private BigDecimal standardWorkHoursPerDay;
//
//    private Boolean isActive;
//}

package com.panoramic.dto.payroll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollConfigurationDto {
    private Long id;

    @NotBlank(message = "Configuration name is required")
    private String configName;

    @NotNull(message = "Provident fund rate is required")
    @DecimalMin(value = "0.0", message = "Provident fund rate must be non-negative")
    private BigDecimal defaultProvidentFundRate;

    @NotNull(message = "Health insurance amount is required")
    @DecimalMin(value = "0.0", message = "Health insurance amount must be non-negative")
    private BigDecimal defaultHealthInsurance;

    @NotNull(message = "Overtime rate multiplier is required")
    @DecimalMin(value = "1.0", message = "Overtime rate multiplier must be at least 1.0")
    private BigDecimal overtimeRateMultiplier;

    @NotNull(message = "Late penalty is required")
    @DecimalMin(value = "0.0", message = "Late penalty must be non-negative")
    private BigDecimal latePenaltyPerOccurrence;

    @NotNull(message = "Absent penalty is required")
    @DecimalMin(value = "0.0", message = "Absent penalty must be non-negative")
    private BigDecimal absentPenaltyPerDay;

    @NotNull(message = "Standard work hours is required")
    @DecimalMin(value = "1.0", message = "Standard work hours must be at least 1.0")
    private BigDecimal standardWorkHoursPerDay;

    // Employment type specific settings
    private BigDecimal fullTimeMonthlyHours;
    private Boolean internProvidentFundApplicable;
    private Boolean partTimeHealthInsuranceProrated;
    private Boolean contractHealthInsuranceApplicable;

    private Boolean isActive;
}
