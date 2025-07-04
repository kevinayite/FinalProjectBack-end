package com.panoramic.repository;

import com.panoramic.model.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findBySkuNumber(String skuNumber);
    Boolean existsBySkuNumber(String skuNumber);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.isActive = true")
    Page<InventoryItem> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.category = :category")
    Page<InventoryItem> findByUserIdAndCategory(@Param("userId") Long userId, 
                                              @Param("category") String category, 
                                              Pageable pageable);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderPoint AND i.isActive = true")
    List<InventoryItem> findLowStockItems();
    
    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND " +
           "(LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.skuNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<InventoryItem> findByUserIdAndSearch(@Param("userId") Long userId, 
                                            @Param("search") String search, 
                                            Pageable pageable);
    List<InventoryItem> findByUserIdAndIsActiveTrue(Long userId);

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity < i.reorderPoint AND i.isActive = true")
    long countLowStockItems();

    // Sum total value of items in stock (quantity * unitPrice)
    @Query("SELECT SUM(i.quantity * i.unitPrice) FROM InventoryItem i WHERE i.isActive = true")
    Double calculateTotalStockValue();
    long countByIsActiveTrue();

    // Count items with quantity == 0
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity = 0 AND i.isActive = true")
    long countOutOfStockItems();

    // Count items where quantity > reorderPoint
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity > i.reorderPoint AND i.isActive = true")
    long countInStockItems();



}
