package com.panoramic.repository;

import com.panoramic.model.Budget;
import com.panoramic.model.BudgetItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {
    // Find all items for a specific budget
    List<BudgetItem> findByBudgetId(Long budgetId);

    // Find all items for a specific budget
    List<BudgetItem> findByBudget(Budget budget);

    // Find all items for a specific budget with pagination
    Page<BudgetItem> findByBudget(Budget budget, Pageable pageable);

    // Find a specific item by ID and budget
    Optional<BudgetItem> findByIdAndBudget(Long id, Budget budget);

    // Delete all items for a specific budget
    void deleteByBudget(Budget budget);

    // Find items by category
    List<BudgetItem> findByCategory(String category);

    // Find items by type (FIXED, VARIABLE, ONE_TIME)
    List<BudgetItem> findByType(String type);
}
