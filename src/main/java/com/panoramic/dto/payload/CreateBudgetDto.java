package com.panoramic.dto.payload;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateBudgetDto(
        String budgetName,
        Integer fiscalYear,
        String description,
        Long departmentId,
        BigDecimal totalAmount,
        LocalDate startDate,
        LocalDate endDate,
        List<CreateBudgetItemDto> items
) {}
