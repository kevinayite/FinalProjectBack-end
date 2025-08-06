//package com.panoramic.service;
//
//import com.panoramic.dto.payload.*;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.*;
//import com.panoramic.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ExpenseService {
//    private final ExpenseRepository expenseRepository;
//    private final BudgetItemRepository budgetItemRepository;
//    private final BudgetRepository budgetRepository;
//
//    @Transactional
//    public ExpenseDto createExpense(CreateExpenseDto dto, Authentication authentication) {
//        BudgetItem item = budgetItemRepository.findById(dto.budgetItemId())
//                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));
//
//        // Check if expense would exceed item allocation
//        BigDecimal newSpentAmount = item.getSpentAmount().add(dto.amount());
//        if (newSpentAmount.compareTo(item.getAllocatedAmount()) > 0) {
//            throw new BadRequestException("Expense would exceed allocated amount for this item");
//        }
//
//        Expense expense = new Expense();
//        expense.setBudgetItem(item);
//        expense.setAmount(dto.amount());
//        expense.setExpenseDate(dto.expenseDate());
//        expense.setDescription(dto.description());
//        expense.setCreatedAt(LocalDateTime.now());
//
//        Expense savedExpense = expenseRepository.save(expense);
//
//        // Update item's spent amount
//        item.setSpentAmount(newSpentAmount);
//        budgetItemRepository.save(item);
//
//        // Update budget's spent amount
//        Budget budget = item.getBudget();
//        budget.setSpentAmount(budget.getSpentAmount().add(dto.amount()));
//        budgetRepository.save(budget);
//
//        return convertToDto(savedExpense);
//    }
//
//    public Page<ExpenseDto> getExpensesByBudgetItem(Long itemId, Authentication authentication, Pageable pageable) {
//        BudgetItem item = budgetItemRepository.findById(itemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));
//
//        return expenseRepository.findByBudgetItem(item, pageable)
//                .map(this::convertToDto);
//    }
//
//    public Page<ExpenseDto> getExpensesByBudget(Long budgetId, Authentication authentication, Pageable pageable) {
//        Budget budget = budgetRepository.findById(budgetId)
//                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
//
//        return expenseRepository.findByBudgetItemBudget(budget, pageable)
//                .map(this::convertToDto);
//    }
//
//    public Page<ExpenseDto> getExpensesBetweenDates(LocalDate start, LocalDate end, Authentication authentication, Pageable pageable) {
//        return expenseRepository.findByExpenseDateBetween(start, end, pageable)
//                .map(this::convertToDto);
//    }
//
//    @Transactional
//    public ExpenseDto updateExpense(Long expenseId, CreateExpenseDto dto, Authentication authentication) {
//        Expense expense = expenseRepository.findById(expenseId)
//                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
//
//        BudgetItem item = expense.getBudgetItem();
//        BigDecimal amountDifference = dto.amount().subtract(expense.getAmount());
//
//        // Check if update would exceed allocation
//        BigDecimal newSpentAmount = item.getSpentAmount().add(amountDifference);
//        if (newSpentAmount.compareTo(item.getAllocatedAmount()) > 0) {
//            throw new BadRequestException("Updated expense would exceed allocated amount");
//        }
//
//        expense.setAmount(dto.amount());
//        expense.setExpenseDate(dto.expenseDate());
//        expense.setDescription(dto.description());
//
//        Expense updatedExpense = expenseRepository.save(expense);
//
//        // Update item's spent amount
//        item.setSpentAmount(newSpentAmount);
//        budgetItemRepository.save(item);
//
//        // Update budget's spent amount
//        Budget budget = item.getBudget();
//        budget.setSpentAmount(budget.getSpentAmount().add(amountDifference));
//        budgetRepository.save(budget);
//
//        return convertToDto(updatedExpense);
//    }
//
//    @Transactional
//    public void deleteExpense(Long expenseId, Authentication authentication) {
//        Expense expense = expenseRepository.findById(expenseId)
//                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
//
//        BudgetItem item = expense.getBudgetItem();
//        Budget budget = item.getBudget();
//
//        // Update amounts
//        item.setSpentAmount(item.getSpentAmount().subtract(expense.getAmount()));
//        budget.setSpentAmount(budget.getSpentAmount().subtract(expense.getAmount()));
//
//        budgetItemRepository.save(item);
//        budgetRepository.save(budget);
//
//        expenseRepository.delete(expense);
//    }
//
//    public ExpenseStatsResponse getExpenseStats(Authentication authentication) {
//        List<Expense> expenses = expenseRepository.findAll();
//
//        BigDecimal totalAmount = expenses.stream()
//                .map(Expense::getAmount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        long count = expenses.size();
//        BigDecimal average = count > 0 ?
//                totalAmount.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP) :
//                BigDecimal.ZERO;
//
//        return new ExpenseStatsResponse(
//                count,
//                totalAmount,
//                average,
//                expenses.stream()
//                        .collect(Collectors.groupingBy(
//                                e -> e.getBudgetItem().getCategory(),
//                                Collectors.reducing(
//                                        BigDecimal.ZERO,
//                                        Expense::getAmount,
//                                        BigDecimal::add
//                                )
//                        ))
//        );
//    }
//
//    private ExpenseDto convertToDto(Expense expense) {
//        return new ExpenseDto(
//                expense.getId(),
//                expense.getBudgetItem().getId(),
//                expense.getBudgetItem().getCategory(),
//                expense.getAmount(),
//                expense.getExpenseDate(),
//                expense.getDescription()
//        );
//    }
//}











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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BudgetItemRepository budgetItemRepository;
    private final BudgetRepository budgetRepository;

    @Transactional
    public ExpenseDto createExpense(CreateExpenseDto dto, Authentication authentication) {
        BudgetItem item = budgetItemRepository.findById(dto.budgetItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));

        // Check if expense would exceed item allocation
        BigDecimal newSpentAmount = item.getSpentAmount().add(dto.amount());
        if (newSpentAmount.compareTo(item.getAllocatedAmount()) > 0) {
            throw new BadRequestException("Expense would exceed allocated amount for this item");
        }

        Expense expense = new Expense();
        expense.setBudgetItem(item);
        expense.setAmount(dto.amount());
        expense.setExpenseDate(dto.expenseDate());
        expense.setDescription(dto.description());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setBudget(item.getBudget());

        Expense savedExpense = expenseRepository.save(expense);

        // Update item's spent amount
        item.setSpentAmount(newSpentAmount);
        budgetItemRepository.save(item);

        // Update budget's spent amount
        Budget budget = item.getBudget();
        budget.setSpentAmount(budget.getSpentAmount().add(dto.amount()));
        budgetRepository.save(budget);

        return convertToDto(savedExpense);
    }

    public Page<ExpenseDto> getExpensesByBudgetItem(Long itemId, Authentication authentication, Pageable pageable) {
        BudgetItem item = budgetItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));

        return expenseRepository.findByBudgetItem(item, pageable)
                .map(this::convertToDto);
    }

    public Page<ExpenseDto> getExpensesByBudget(Long budgetId, Authentication authentication, Pageable pageable) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        return expenseRepository.findByBudgetItemBudget(budget, pageable)
                .map(this::convertToDto);
    }

    public Page<ExpenseDto> getExpensesBetweenDates(LocalDate start, LocalDate end, Authentication authentication, Pageable pageable) {
        return expenseRepository.findByExpenseDateBetween(start, end, pageable)
                .map(this::convertToDto);
    }

    @Transactional
    public ExpenseDto updateExpense(Long expenseId, CreateExpenseDto dto, Authentication authentication) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        BudgetItem oldItem = expense.getBudgetItem();

        // Check if budget item is changing
        if (!oldItem.getId().equals(dto.budgetItemId())) {
            // Handle moving expense to different budget item
            BudgetItem newItem = budgetItemRepository.findById(dto.budgetItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Budget item not found"));

            // Validate new item can accommodate the expense
            BigDecimal newItemSpent = newItem.getSpentAmount().add(dto.amount());
            if (newItemSpent.compareTo(newItem.getAllocatedAmount()) > 0) {
                throw new BadRequestException("Expense would exceed allocated amount for new item");
            }

            // Update old item (subtract old amount)
            oldItem.setSpentAmount(oldItem.getSpentAmount().subtract(expense.getAmount()));
            Budget oldBudget = oldItem.getBudget();
            oldBudget.setSpentAmount(oldBudget.getSpentAmount().subtract(expense.getAmount()));

            // Update new item (add new amount)
            newItem.setSpentAmount(newItemSpent);
            Budget newBudget = newItem.getBudget();
            newBudget.setSpentAmount(newBudget.getSpentAmount().add(dto.amount()));

            // Update expense with new values
            expense.setBudgetItem(newItem);
            expense.setAmount(dto.amount());
            expense.setExpenseDate(dto.expenseDate());
            expense.setDescription(dto.description());

            Expense updatedExpense = expenseRepository.save(expense);

            // Save all affected entities
            budgetItemRepository.save(oldItem);
            budgetItemRepository.save(newItem);
            budgetRepository.save(oldBudget);
            if (!oldBudget.getId().equals(newBudget.getId())) {
                budgetRepository.save(newBudget);
            }

            return convertToDto(updatedExpense);
        } else {
            // Existing logic for same budget item
            BigDecimal amountDifference = dto.amount().subtract(expense.getAmount());

            // Check if update would exceed allocation
            BigDecimal newSpentAmount = oldItem.getSpentAmount().add(amountDifference);
            if (newSpentAmount.compareTo(oldItem.getAllocatedAmount()) > 0) {
                throw new BadRequestException("Updated expense would exceed allocated amount");
            }

            // Update expense
            expense.setAmount(dto.amount());
            expense.setExpenseDate(dto.expenseDate());
            expense.setDescription(dto.description());

            Expense updatedExpense = expenseRepository.save(expense);

            // Update item's spent amount
            oldItem.setSpentAmount(newSpentAmount);
            budgetItemRepository.save(oldItem);

            // Update budget's spent amount
            Budget budget = oldItem.getBudget();
            budget.setSpentAmount(budget.getSpentAmount().add(amountDifference));
            budgetRepository.save(budget);

            return convertToDto(updatedExpense);
        }
    }

    @Transactional
    public void deleteExpense(Long expenseId, Authentication authentication) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        BudgetItem item = expense.getBudgetItem();
        Budget budget = item.getBudget();

        // Update amounts
        item.setSpentAmount(item.getSpentAmount().subtract(expense.getAmount()));
        budget.setSpentAmount(budget.getSpentAmount().subtract(expense.getAmount()));

        budgetItemRepository.save(item);
        budgetRepository.save(budget);

        expenseRepository.delete(expense);
    }

    public ExpenseStatsResponse getExpenseStats(Authentication authentication) {
        List<Expense> expenses = expenseRepository.findAll();

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long count = expenses.size();

        BigDecimal average = count > 0 ?
                totalAmount.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        return new ExpenseStatsResponse(
                count,
                totalAmount,
                average,
                expenses.stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getBudgetItem().getCategory(),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Expense::getAmount,
                                        BigDecimal::add
                                )
                        ))
        );
    }

    private ExpenseDto convertToDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getBudgetItem().getId(),
                expense.getBudgetItem().getCategory(),
                expense.getAmount(),
                expense.getExpenseDate(),
                expense.getDescription()
        );
    }
}
