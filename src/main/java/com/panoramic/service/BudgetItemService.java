package com.panoramic.service;

import com.panoramic.dto.payload.*;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.*;
import com.panoramic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BudgetItemService {
    private final BudgetItemRepository budgetItemRepository;
    private final BudgetRepository budgetRepository;

    public Page<BudgetItemDto> getBudgetItems(Long budgetId, Authentication authentication, Pageable pageable) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        return budgetItemRepository.findByBudget(budget, pageable)
                .map(this::convertToDto);
    }

    @Transactional
    public BudgetItemDto createBudgetItem(Long budgetId, CreateBudgetItemDto dto, Authentication authentication) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        // Check if adding this item would exceed budget total
        BigDecimal newAllocated = budget.getAllocatedAmount().add(dto.allocatedAmount());
        if (newAllocated.compareTo(budget.getTotalAmount()) > 0) {
            throw new BadRequestException("Adding this item would exceed budget total");
        }

        BudgetItem item = new BudgetItem();
        item.setBudget(budget);
        item.setCategory(dto.category());
        item.setDescription(dto.description());
        item.setAllocatedAmount(dto.allocatedAmount());
        item.setSpentAmount(BigDecimal.ZERO);
        item.setType(dto.type());

        BudgetItem savedItem = budgetItemRepository.save(item);

        // Update budget's allocated amount
        budget.setAllocatedAmount(newAllocated);
        budgetRepository.save(budget);

        return convertToDto(savedItem);
    }

    @Transactional
    public BudgetItemDto updateBudgetItem(Long budgetId, Long itemId, CreateBudgetItemDto dto, Authentication authentication) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        BudgetItem item = budgetItemRepository.findByIdAndBudget(itemId, budget)
                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));

        // Calculate new allocated amount difference
        BigDecimal amountDifference = dto.allocatedAmount().subtract(item.getAllocatedAmount());
        BigDecimal newBudgetAllocated = budget.getAllocatedAmount().add(amountDifference);

        if (newBudgetAllocated.compareTo(budget.getTotalAmount()) > 0) {
            throw new BadRequestException("Updating this item would exceed budget total");
        }

        item.setCategory(dto.category());
        item.setDescription(dto.description());
        item.setAllocatedAmount(dto.allocatedAmount());
        item.setType(dto.type());

        BudgetItem updatedItem = budgetItemRepository.save(item);

        // Update budget's allocated amount
        budget.setAllocatedAmount(newBudgetAllocated);
        budgetRepository.save(budget);

        return convertToDto(updatedItem);
    }

    @Transactional
    public void deleteBudgetItem(Long budgetId, Long itemId, Authentication authentication) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        BudgetItem item = budgetItemRepository.findByIdAndBudget(itemId, budget)
                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));

        // Update budget's allocated amount
        budget.setAllocatedAmount(budget.getAllocatedAmount().subtract(item.getAllocatedAmount()));
        budgetRepository.save(budget);

        budgetItemRepository.delete(item);
    }

    public Page<ExpenseSummaryDto> getBudgetItemSummaries(Long budgetId, Authentication authentication, Pageable pageable) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        return budgetItemRepository.findByBudget(budget, pageable)
                .map(item -> new ExpenseSummaryDto(
                        item.getCategory(),
                        item.getAllocatedAmount(),
                        item.getSpentAmount(),
                        item.getAllocatedAmount().subtract(item.getSpentAmount())
                ));
    }

    private BudgetItemDto convertToDto(BudgetItem item) {
        return new BudgetItemDto(
                item.getId(),
                item.getCategory(),
                item.getDescription(),
                item.getAllocatedAmount(),
                item.getSpentAmount(),
                item.getType(),
                item.getAllocatedAmount().subtract(item.getSpentAmount())
        );
    }
}
