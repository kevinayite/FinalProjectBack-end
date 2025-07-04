package com.panoramic.dto.payload;


import java.math.BigDecimal;

public record ExpenseSummaryDto(
        String category,
        BigDecimal allocatedAmount,
        BigDecimal spentAmount,
        BigDecimal remainingAmount
) {}