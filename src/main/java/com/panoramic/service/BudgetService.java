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
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final DepartmentRepository departmentRepository;
    private final BudgetItemRepository budgetItemRepository;

    @Transactional
    public BudgetDto createBudget(CreateBudgetDto dto, Authentication authentication) {
        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        // Validate total vs allocated amounts
        BigDecimal totalAllocated = dto.items().stream()
                .map(CreateBudgetItemDto::allocatedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalAllocated.compareTo(dto.totalAmount()) > 0) {
            throw new BadRequestException("Total allocated amount exceeds budget total");
        }

        Budget budget = new Budget();
        budget.setBudgetName(dto.budgetName());
        budget.setFiscalYear(dto.fiscalYear());
        budget.setDescription(dto.description());
        budget.setDepartment(department);
        budget.setTotalAmount(dto.totalAmount());
        budget.setAllocatedAmount(totalAllocated);
        budget.setStartDate(dto.startDate());
        budget.setEndDate(dto.endDate());
        budget.setCreatedAt(LocalDateTime.now());

        Budget savedBudget = budgetRepository.save(budget);

        // Save budget items
        List<BudgetItemDto> itemDtos = dto.items().stream()
                .map(itemDto -> createBudgetItem(savedBudget, itemDto))
                .toList();

        return convertToDto(savedBudget, itemDtos);
    }

    public Page<BudgetSummaryDto> getBudgets(Authentication authentication, Pageable pageable) {
        return budgetRepository.findAll(pageable)
                .map(this::convertToSummaryDto);
    }

    public BudgetDto getBudgetById(Long id, Authentication authentication) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        List<BudgetItemDto> items = budgetItemRepository.findByBudget(budget).stream()
                .map(this::convertToItemDto)
                .toList();

        return convertToDto(budget, items);
    }

    @Transactional
    public BudgetDto updateBudget(Long id, CreateBudgetDto dto, Authentication authentication) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        budget.setBudgetName(dto.budgetName());
        budget.setFiscalYear(dto.fiscalYear());
        budget.setDescription(dto.description());
        budget.setDepartment(department);

        BigDecimal currentAllocated = budget.getAllocatedAmount();
        if (currentAllocated.compareTo(dto.totalAmount()) > 0) {
            throw new BadRequestException("Cannot reduce total amount below currently allocated amount");
        }


        budget.setTotalAmount(dto.totalAmount());
        budget.setStartDate(dto.startDate());
        budget.setEndDate(dto.endDate());

        Budget updatedBudget = budgetRepository.save(budget);

        List<BudgetItemDto> items = budgetItemRepository.findByBudget(budget).stream()
                .map(this::convertToItemDto)
                .toList();

        return convertToDto(updatedBudget, items);
    }

    @Transactional
    public void deleteBudget(Long id, Authentication authentication) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        if (budget.getSpentAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Cannot delete budget with existing expenses");
        }

        budgetItemRepository.deleteByBudget(budget);
        budgetRepository.delete(budget);
    }

    public Page<BudgetSummaryDto> searchBudgets(String search, Authentication authentication, Pageable pageable) {
        return budgetRepository.findByBudgetNameContaining(search, pageable)
                .map(this::convertToSummaryDto);
    }

    public BudgetStatsResponse getBudgetStats(Authentication authentication) {
        List<Budget> budgets = budgetRepository.findAll();

        BigDecimal totalAmount = budgets.stream()
                .map(Budget::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal spentAmount = budgets.stream()
                .map(Budget::getSpentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BudgetStatsResponse(
                (long)budgets.size(),
                totalAmount,
                spentAmount,
                totalAmount.compareTo(BigDecimal.ZERO) > 0 ?
                        spentAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)) :
                        BigDecimal.ZERO
        );
    }

    private BudgetItemDto createBudgetItem(Budget budget, CreateBudgetItemDto dto) {
        BudgetItem item = new BudgetItem();
        item.setBudget(budget);
        item.setCategory(dto.category());
        item.setDescription(dto.description());
        item.setAllocatedAmount(dto.allocatedAmount());
        item.setSpentAmount(BigDecimal.ZERO);
        item.setType(dto.type());

        BudgetItem savedItem = budgetItemRepository.save(item);
        return convertToItemDto(savedItem);
    }

    private BudgetDto convertToDto(Budget budget, List<BudgetItemDto> items) {
        return new BudgetDto(
                budget.getId(),
                budget.getBudgetName(),
                budget.getFiscalYear(),
                budget.getDescription(),
                budget.getDepartment().getId(),
                budget.getDepartment().getName(),
                budget.getTotalAmount(),
                budget.getAllocatedAmount(),
                budget.getSpentAmount(),
                budget.getStartDate(),
                budget.getEndDate(),
                items
        );
    }

    private BudgetSummaryDto convertToSummaryDto(Budget budget) {
        return new BudgetSummaryDto(
                budget.getId(),
                budget.getBudgetName(),
                budget.getTotalAmount(),
                budget.getAllocatedAmount(),
                budget.getSpentAmount(),
                budget.getTotalAmount().compareTo(BigDecimal.ZERO) > 0 ?
                        budget.getSpentAmount().divide(budget.getTotalAmount(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(BigDecimal.valueOf(100)) :
                        BigDecimal.ZERO
        );
    }

    private BudgetItemDto convertToItemDto(BudgetItem item) {
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
