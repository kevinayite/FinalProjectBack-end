package com.panoramic.dto.payload;

import java.math.BigDecimal;

public record BudgetStatsResponse(
        Long totalBudgets,
        BigDecimal totalAmount,
        BigDecimal totalSpent,
        BigDecimal averageUtilization
) {}
