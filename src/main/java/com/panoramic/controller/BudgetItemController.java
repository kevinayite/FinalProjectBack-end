package com.panoramic.controller;

import com.panoramic.dto.payload.*;
import com.panoramic.service.BudgetItemService;
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
@RequestMapping("/api/budgets/{budgetId}/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BudgetItemController {

    private final BudgetItemService budgetItemService;

    @GetMapping
    public ResponseEntity<Page<BudgetItemDto>> getBudgetItems(
            @PathVariable Long budgetId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Authentication authentication) {
        Page<BudgetItemDto> budgetItems = budgetItemService.getBudgetItems(budgetId, authentication, pageable);
        return ResponseEntity.ok(budgetItems);
    }

    @PostMapping
    public ResponseEntity<BudgetItemDto> createBudgetItem(
            @PathVariable Long budgetId,
            @Valid @RequestBody CreateBudgetItemDto createBudgetItemDto,
            Authentication authentication) {
        BudgetItemDto budgetItem = budgetItemService.createBudgetItem(budgetId, createBudgetItemDto, authentication);
        return new ResponseEntity<>(budgetItem, HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<BudgetItemDto> updateBudgetItem(
            @PathVariable Long budgetId,
            @PathVariable Long itemId,
            @Valid @RequestBody CreateBudgetItemDto updateBudgetItemDto,
            Authentication authentication) {
        BudgetItemDto budgetItem = budgetItemService.updateBudgetItem(budgetId, itemId, updateBudgetItemDto, authentication);
        return ResponseEntity.ok(budgetItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteBudgetItem(
            @PathVariable Long budgetId,
            @PathVariable Long itemId,
            Authentication authentication) {
        budgetItemService.deleteBudgetItem(budgetId, itemId, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summaries")
    public ResponseEntity<Page<ExpenseSummaryDto>> getBudgetItemSummaries(
            @PathVariable Long budgetId,
            @PageableDefault(size = 10, sort = "category") Pageable pageable,
            Authentication authentication) {
        Page<ExpenseSummaryDto> summaries = budgetItemService.getBudgetItemSummaries(budgetId, authentication, pageable);
        return ResponseEntity.ok(summaries);
    }
}
