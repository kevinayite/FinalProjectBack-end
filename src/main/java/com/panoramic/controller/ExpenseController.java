package com.panoramic.controller;

import com.panoramic.dto.payload.*;
import com.panoramic.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(
            @Valid @RequestBody CreateExpenseDto createExpenseDto,
            Authentication authentication) {
        ExpenseDto expense = expenseService.createExpense(createExpenseDto, authentication);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    @GetMapping("/budget-item/{itemId}")
    public ResponseEntity<Page<ExpenseDto>> getExpensesByBudgetItem(
            @PathVariable Long itemId,
            @PageableDefault(size = 10, sort = "expenseDate") Pageable pageable,
            Authentication authentication) {
        Page<ExpenseDto> expenses = expenseService.getExpensesByBudgetItem(itemId, authentication, pageable);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/budget/{budgetId}")
    public ResponseEntity<Page<ExpenseDto>> getExpensesByBudget(
            @PathVariable Long budgetId,
            @PageableDefault(size = 10, sort = "expenseDate") Pageable pageable,
            Authentication authentication) {
        Page<ExpenseDto> expenses = expenseService.getExpensesByBudget(budgetId, authentication, pageable);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<ExpenseDto>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "expenseDate") Pageable pageable,
            Authentication authentication) {
        Page<ExpenseDto> expenses = expenseService.getExpensesBetweenDates(startDate, endDate, authentication, pageable);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDto> updateExpense(
            @PathVariable Long expenseId,
            @Valid @RequestBody CreateExpenseDto updateExpenseDto,
            Authentication authentication) {
        ExpenseDto expense = expenseService.updateExpense(expenseId, updateExpenseDto, authentication);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long expenseId,
            Authentication authentication) {
        expenseService.deleteExpense(expenseId, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<ExpenseStatsResponse> getExpenseStats(Authentication authentication) {
        ExpenseStatsResponse stats = expenseService.getExpenseStats(authentication);
        return ResponseEntity.ok(stats);
    }
}
