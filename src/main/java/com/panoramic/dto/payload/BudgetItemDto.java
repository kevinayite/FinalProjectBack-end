package com.panoramic.dto.payload;

import com.panoramic.model.enums.BudgetItemType;

import java.math.BigDecimal;

public record BudgetItemDto(
        Long id,
        String category,
        String description,
        BigDecimal allocatedAmount,
        BigDecimal spentAmount,
        BudgetItemType type,
        BigDecimal remainingAmount
) {}