//package com.panoramic.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.panoramic.model.enums.BudgetItemType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "budget_items")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class BudgetItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "budget_id", nullable = false)
//    @JsonBackReference
//    private Budget budget;
//
//    @Column(nullable = false)
//    private String category;
//
//    @Column(nullable = false)
//    private String description;
//
//    @Column(nullable = false)
//    private BigDecimal amount;
//
//    @Enumerated(EnumType.STRING)
//    private BudgetItemType type;
//}



package com.panoramic.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.panoramic.model.enums.BudgetItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "budget_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    @JsonBackReference
    private Budget budget;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column(name = "allocated_amount", nullable = false)
    private BigDecimal allocatedAmount;

    @Column(name = "spent_amount", nullable = false)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private BudgetItemType type;

    @OneToMany(mappedBy = "budgetItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Expense> expenses;

    // Helper method to get remaining amount for this item
    public BigDecimal getRemainingAmount() {
        return allocatedAmount.subtract(spentAmount);
    }
}