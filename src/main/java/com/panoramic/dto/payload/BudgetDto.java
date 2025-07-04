package com.panoramic.dto.payload;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BudgetDto(
        Long id,
        String budgetName,
        Integer fiscalYear,
        String description,
        Long departmentId,
        String departmentName,
        BigDecimal totalAmount,
        BigDecimal allocatedAmount,
        BigDecimal spentAmount,
        LocalDate startDate,
        LocalDate endDate,
        List<BudgetItemDto> items
) {}
