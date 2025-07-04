package com.panoramic.dto.payload;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseDto(
        Long budgetItemId,
        BigDecimal amount,
        LocalDate expenseDate,
        String description
) {}