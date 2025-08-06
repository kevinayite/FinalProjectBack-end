//package com.panoramic.repository;
//
//import com.panoramic.model.InventoryItem;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
//    Optional<InventoryItem> findBySkuNumber(String skuNumber);
//    Boolean existsBySkuNumber(String skuNumber);
//
//    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.isActive = true")
//    Page<InventoryItem> findByUserId(@Param("userId") Long userId, Pageable pageable);
//
//    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.category = :category")
//    Page<InventoryItem> findByUserIdAndCategory(@Param("userId") Long userId,
//                                              @Param("category") String category,
//                                              Pageable pageable);
//
//    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderPoint AND i.isActive = true")
//    List<InventoryItem> findLowStockItems();
//
//    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND " +
//           "(LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//           "LOWER(i.skuNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//           "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%')))")
//    Page<InventoryItem> findByUserIdAndSearch(@Param("userId") Long userId,
//                                            @Param("search") String search,
//                                            Pageable pageable);
//    List<InventoryItem> findByUserIdAndIsActiveTrue(Long userId);
//
//    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity < i.reorderPoint AND i.isActive = true")
//    long countLowStockItems();
//
//    // Sum total value of items in stock (quantity * unitPrice)
//    @Query("SELECT SUM(i.quantity * i.unitPrice) FROM InventoryItem i WHERE i.isActive = true")
//    Double calculateTotalStockValue();
//    long countByIsActiveTrue();
//
//    // Count items with quantity == 0
//    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity = 0 AND i.isActive = true")
//    long countOutOfStockItems();
//
//    // Count items where quantity > reorderPoint
//    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity > i.reorderPoint AND i.isActive = true")
//    long countInStockItems();
//
//
//
//}












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

    // Updated: Get all active inventory items (removed user filtering)
    @Query("SELECT i FROM InventoryItem i WHERE i.isActive = true")
    Page<InventoryItem> findAllActiveItems(Pageable pageable);

    // Updated: Get all inventory items regardless of user
    @Query("SELECT i FROM InventoryItem i")
    Page<InventoryItem> findAllItems(Pageable pageable);

    // Updated: Get items by category (removed user filtering)
    @Query("SELECT i FROM InventoryItem i WHERE i.category = :category AND i.isActive = true")
    Page<InventoryItem> findByCategory(@Param("category") String category, Pageable pageable);

    // Updated: Search across all items (removed user filtering)
    @Query("SELECT i FROM InventoryItem i WHERE i.isActive = true AND " +
            "(LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.skuNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<InventoryItem> findBySearch(@Param("search") String search, Pageable pageable);

    // Updated: Get all active items (removed user filtering)
    @Query("SELECT i FROM InventoryItem i WHERE i.isActive = true")
    List<InventoryItem> findAllActiveItems();

    // Existing queries that already work globally
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderPoint AND i.isActive = true")
    List<InventoryItem> findLowStockItems();

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity < i.reorderPoint AND i.isActive = true")
    long countLowStockItems();

    @Query("SELECT SUM(i.quantity * i.unitPrice) FROM InventoryItem i WHERE i.isActive = true")
    Double calculateTotalStockValue();

    long countByIsActiveTrue();

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity = 0 AND i.isActive = true")
    long countOutOfStockItems();

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity > i.reorderPoint AND i.isActive = true")
    long countInStockItems();

    // Additional useful queries
    @Query("SELECT DISTINCT i.category FROM InventoryItem i WHERE i.isActive = true ORDER BY i.category")
    List<String> findAllCategories();

    @Query("SELECT i FROM InventoryItem i WHERE i.quantity > 0 AND i.isActive = true")
    List<InventoryItem> findAvailableItems();

    // Add these methods to the existing InventoryItemRepository interface

    // Get out of stock items
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity = 0 AND i.isActive = true")
    Page<InventoryItem> findOutOfStockItems(Pageable pageable);

    // Get in stock items
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity > i.reorderPoint AND i.isActive = true")
    Page<InventoryItem> findInStockItems(Pageable pageable);

    // Find items by price range
    @Query("SELECT i FROM InventoryItem i WHERE i.unitPrice BETWEEN :minPrice AND :maxPrice AND i.isActive = true")
    Page<InventoryItem> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

    // Find items by quantity range
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity BETWEEN :minQuantity AND :maxQuantity AND i.isActive = true")
    Page<InventoryItem> findByQuantityRange(@Param("minQuantity") Integer minQuantity, @Param("maxQuantity") Integer maxQuantity, Pageable pageable);

    // Find items created by specific user (for audit purposes)
    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.isActive = true")
    Page<InventoryItem> findItemsCreatedByUser(@Param("userId") Long userId, Pageable pageable);

    // Find recently added items (last 30 days)
    @Query("SELECT i FROM InventoryItem i WHERE i.createdAt >= :date AND i.isActive = true ORDER BY i.createdAt DESC")
    Page<InventoryItem> findRecentlyAddedItems(@Param("date") java.time.LocalDateTime date, Pageable pageable);

    // Find items that need reordering (quantity <= reorder point)
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderPoint AND i.isActive = true ORDER BY (i.reorderPoint - i.quantity) DESC")
    Page<InventoryItem> findItemsNeedingReorder(Pageable pageable);

}
