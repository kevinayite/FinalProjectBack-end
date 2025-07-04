package com.panoramic.controller;

import com.panoramic.dto.payload.*;
import com.panoramic.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetDto> createBudget(
            @Valid @RequestBody CreateBudgetDto createBudgetDto,
            Authentication authentication) {
        BudgetDto budget = budgetService.createBudget(createBudgetDto, authentication);
        return new ResponseEntity<>(budget, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<BudgetSummaryDto>> getAllBudgets(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Authentication authentication) {
        Page<BudgetSummaryDto> budgets = budgetService.getBudgets(authentication, pageable);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDto> getBudgetById(
            @PathVariable Long id,
            Authentication authentication) {
        BudgetDto budget = budgetService.getBudgetById(id, authentication);
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody CreateBudgetDto updateBudgetDto,
            Authentication authentication) {
        BudgetDto budget = budgetService.updateBudget(id, updateBudgetDto, authentication);
        return ResponseEntity.ok(budget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id,
            Authentication authentication) {
        budgetService.deleteBudget(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BudgetSummaryDto>> searchBudgets(
            @RequestParam String query,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Authentication authentication) {
        Page<BudgetSummaryDto> budgets = budgetService.searchBudgets(query, authentication, pageable);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/stats")
    public ResponseEntity<BudgetStatsResponse> getBudgetStats(Authentication authentication) {
        BudgetStatsResponse stats = budgetService.getBudgetStats(authentication);
        return ResponseEntity.ok(stats);
    }
}
