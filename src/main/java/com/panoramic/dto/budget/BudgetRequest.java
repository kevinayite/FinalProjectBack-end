package com.panoramic.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BudgetRequest {
    @NotBlank(message = "Budget name is required")
    private String budgetName;

    @NotNull(message = "Fiscal year is required")
    private Integer fiscalYear;

    private String description;

    @NotNull(message = "Department is required")
    @Positive(message = "Department ID must be positive")
    private Long departmentId;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotEmpty(message = "Budget items are required")
    private List<BudgetItemRequest> budgetItems;
}
