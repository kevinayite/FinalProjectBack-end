package com.panoramic.dto.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetSummaryDto(
        Long id,
        String budgetName,
        BigDecimal totalAmount,
        BigDecimal allocatedAmount,
        String departmentName,
        LocalDateTime createdAt,
        BigDecimal spentAmount,
        BigDecimal utilizationPercentage
) {}