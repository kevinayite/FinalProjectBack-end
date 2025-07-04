package com.panoramic.repository;

import com.panoramic.model.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // Find budgets by department
    List<Budget> findByDepartmentId(Long departmentId);

    // Find budgets by fiscal year
    List<Budget> findByFiscalYear(Integer fiscalYear);

    // Search budgets by name
    Page<Budget> findByBudgetNameContaining(String budgetName, Pageable pageable);

    // Custom query to find budgets where allocated amount is less than total
    @Query("SELECT b FROM Budget b WHERE b.allocatedAmount < b.totalAmount")
    List<Budget> findUnderallocatedBudgets();
}
