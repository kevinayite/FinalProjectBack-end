package com.panoramic.repository;

import com.panoramic.model.Budget;
import com.panoramic.model.BudgetItem;
import com.panoramic.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Find expenses for a specific budget item
    List<Expense> findByBudgetItemId(Long budgetItemId);

    // Find expenses for a specific budget item with pagination
    Page<Expense> findByBudgetItem(BudgetItem budgetItem, Pageable pageable);

    // Find expenses within a date range
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);

    // Find expenses within a date range with pagination
    Page<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Find expenses by budget (through budget item relationship)
    @Query("SELECT e FROM Expense e WHERE e.budgetItem.budget.id = :budgetId")
    List<Expense> findByBudgetId(Long budgetId);

    // Find expenses by budget with pagination
    Page<Expense> findByBudgetItemBudget(Budget budget, Pageable pageable);

    // Custom query to find large expenses (e.g., > $1000)
    @Query("SELECT e FROM Expense e WHERE e.amount > :threshold")
    List<Expense> findLargeExpenses(BigDecimal threshold);
}
