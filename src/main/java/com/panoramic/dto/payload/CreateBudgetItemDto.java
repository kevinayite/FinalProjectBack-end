package com.panoramic.dto.payload;

import com.panoramic.model.enums.BudgetItemType;

import java.math.BigDecimal;

public record CreateBudgetItemDto(
        String category,
        String description,
        BigDecimal allocatedAmount,
        BudgetItemType type
) {}