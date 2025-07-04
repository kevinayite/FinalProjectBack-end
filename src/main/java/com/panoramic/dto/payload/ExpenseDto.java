package com.panoramic.dto.payload;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDto(
        Long id,
        Long budgetItemId,
        String budgetItemCategory,
        BigDecimal amount,
        LocalDate expenseDate,
        String description
) {}