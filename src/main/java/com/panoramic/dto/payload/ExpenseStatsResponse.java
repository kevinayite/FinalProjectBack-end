package com.panoramic.dto.payload;

import java.math.BigDecimal;
import java.util.Map;

public record ExpenseStatsResponse(
        Long totalExpenses,
        BigDecimal totalAmount,
        BigDecimal averageExpense,
        Map<String, BigDecimal> categoryTotals
) {}
