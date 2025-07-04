package com.panoramic.dto.payload;

import java.math.BigDecimal;

public record BudgetSummaryDto(
        Long id,
        String budgetName,
        BigDecimal totalAmount,
        BigDecimal allocatedAmount,
        BigDecimal spentAmount,
        BigDecimal utilizationPercentage
) {}