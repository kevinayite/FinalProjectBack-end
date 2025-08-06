//package com.panoramic.controller;
//
//import com.panoramic.dto.inventory.InventoryItemRequest;
//import com.panoramic.dto.inventory.InventoryItemResponse;
//import com.panoramic.dto.inventory.InventoryStatsResponse;
//import com.panoramic.model.InventoryItem;
//import com.panoramic.service.InventoryService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/inventory")
//@RequiredArgsConstructor
//public class InventoryController {
//
//    private final InventoryService inventoryService;
//
//    @PostMapping
//    public ResponseEntity<InventoryItem> createInventoryItem(@Valid @RequestBody InventoryItemRequest request,
//                                                           Authentication authentication) {
//        InventoryItem item = inventoryService.createInventoryItem(request, authentication);
//        return ResponseEntity.status(HttpStatus.CREATED).body(item);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<InventoryItem>> getInventoryItems(Authentication authentication, Pageable pageable) {
//        Page<InventoryItem> items = inventoryService.getInventoryItemsInternal(authentication, pageable);
//        return ResponseEntity.ok(items);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id, Authentication authentication) {
//        InventoryItem item = inventoryService.getInventoryItemByIdInternal(id, authentication);
//        return ResponseEntity.ok(item);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id,
//                                                           @Valid @RequestBody InventoryItemRequest request,
//                                                           Authentication authentication) {
//        InventoryItem item = inventoryService.updateInventoryItem(id, request, authentication);
//        return ResponseEntity.ok(item);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id, Authentication authentication) {
//        inventoryService.deleteInventoryItem(id, authentication);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<InventoryItem>> searchInventoryItems(@RequestParam String search,
//                                                                  Authentication authentication,
//                                                                  Pageable pageable) {
//        Page<InventoryItem> items = inventoryService.searchInventoryItems(search, authentication, pageable);
//        return ResponseEntity.ok(items);
//    }
//
//    @GetMapping("/low-stock")
//    public ResponseEntity<List<InventoryItem>> getLowStockItems() {
//        List<InventoryItem> items = inventoryService.getLowStockItems();
//        return ResponseEntity.ok(items);
//    }
//
//    @GetMapping("/stats")
//    public ResponseEntity<InventoryStatsResponse> getInventoryStats(Authentication authentication) {
//        InventoryStatsResponse stats = inventoryService.getInventoryStats(authentication);
//        return ResponseEntity.ok(stats);
//    }
//
//    @GetMapping("/available")
//    public ResponseEntity<List<InventoryItemResponse>> getAvailableInventoryItems(Authentication authentication) {
//        List<InventoryItemResponse> items = inventoryService.getAvailableInventoryItems(authentication);
//        return ResponseEntity.ok(items);
//    }
//
//}














package com.panoramic.controller;

import com.panoramic.dto.inventory.InventoryItemRequest;
import com.panoramic.dto.inventory.InventoryItemResponse;
import com.panoramic.dto.inventory.InventoryStatsResponse;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.InventoryItem;
import com.panoramic.repository.InventoryItemRepository;
import com.panoramic.service.InventoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryItemRepository inventoryItemRepository;

    @PostMapping
    public ResponseEntity<InventoryItem> createInventoryItem(@Valid @RequestBody InventoryItemRequest request,
                                                             Authentication authentication) {
        InventoryItem item = inventoryService.createInventoryItem(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    // Updated: Returns all active inventory items for any authenticated user
    @GetMapping
    public ResponseEntity<Page<InventoryItem>> getInventoryItems(Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryService.getInventoryItemsInternal(authentication, pageable);
        return ResponseEntity.ok(items);
    }

    // Updated: Any authenticated user can access any inventory item
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id, Authentication authentication) {
        InventoryItem item = inventoryService.getInventoryItemByIdInternal(id, authentication);
        return ResponseEntity.ok(item);
    }

    // Updated: Any authenticated user can update any inventory item
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id,
                                                             @Valid @RequestBody InventoryItemRequest request,
                                                             Authentication authentication) {
        InventoryItem item = inventoryService.updateInventoryItem(id, request, authentication);
        return ResponseEntity.ok(item);
    }

    // Updated: Any authenticated user can delete any inventory item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id, Authentication authentication) {
        inventoryService.deleteInventoryItem(id, authentication);
        return ResponseEntity.noContent().build();
    }

    // Updated: Search across all inventory items
    @GetMapping("/search")
    public ResponseEntity<Page<InventoryItem>> searchInventoryItems(@RequestParam String search,
                                                                    Authentication authentication,
                                                                    Pageable pageable) {
        Page<InventoryItem> items = inventoryService.searchInventoryItems(search, authentication, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryItem>> getLowStockItems() {
        List<InventoryItem> items = inventoryService.getLowStockItems();
        return ResponseEntity.ok(items);
    }

    // Already works globally - returns global inventory statistics
    @GetMapping("/stats")
    public ResponseEntity<InventoryStatsResponse> getInventoryStats(Authentication authentication) {
        InventoryStatsResponse stats = inventoryService.getInventoryStats(authentication);
        return ResponseEntity.ok(stats);
    }

    // Updated: Returns all available inventory items for any authenticated user
    @GetMapping("/available")
    public ResponseEntity<List<InventoryItemResponse>> getAvailableInventoryItems(Authentication authentication) {
        List<InventoryItemResponse> items = inventoryService.getAvailableInventoryItems(authentication);
        return ResponseEntity.ok(items);
    }

    // New: Get inventory items by category
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<InventoryItemResponse>> getInventoryItemsByCategory(
            @PathVariable String category,
            Authentication authentication,
            Pageable pageable) {
        Page<InventoryItemResponse> items = inventoryService.getInventoryItemsByCategory(category, authentication, pageable);
        return ResponseEntity.ok(items);
    }

    // New: Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories(Authentication authentication) {
        List<String> categories = inventoryService.getAllCategories(authentication);
        return ResponseEntity.ok(categories);
    }

    // New: Get all inventory items including inactive ones
    @GetMapping("/all")
    public ResponseEntity<Page<InventoryItemResponse>> getAllInventoryItems(
            Authentication authentication,
            Pageable pageable) {
        Page<InventoryItemResponse> items = inventoryService.getAllInventoryItems(authentication, pageable);
        return ResponseEntity.ok(items);
    }

    // New: Update only quantity (useful for stock adjustments)
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryItem> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            Authentication authentication) {
        InventoryItem item = inventoryService.updateQuantity(id, quantity, authentication);
        return ResponseEntity.ok(item);
    }

    // New: Get items that are out of stock
    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<InventoryItemResponse>> getOutOfStockItems(
            Authentication authentication,
            Pageable pageable) {
        // This would require a new service method
        Page<InventoryItemResponse> items = inventoryService.getOutOfStockItems(authentication, pageable);
        return ResponseEntity.ok(items);
    }

    // New: Get items that are in stock (quantity > reorder point)
    @GetMapping("/in-stock")
    public ResponseEntity<Page<InventoryItemResponse>> getInStockItems(
            Authentication authentication,
            Pageable pageable) {
        Page<InventoryItemResponse> items = inventoryService.getInStockItems(authentication, pageable);
        return ResponseEntity.ok(items);
    }

    // Add this method to your existing InventoryService class

    @Transactional
    public void reduceInventoryQuantity(Long inventoryItemId, Integer quantityUsed, Authentication authentication) {
        InventoryItem item = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + inventoryItemId));

        if (item.getQuantity() < quantityUsed) {
            throw new BadRequestException("Insufficient stock. Available: " + item.getQuantity() + ", Required: " + quantityUsed);
        }

        item.setQuantity(item.getQuantity() - quantityUsed);
        inventoryItemRepository.save(item);
    }

    @Transactional
    public void bulkReduceInventoryQuantities(List<Long> itemIds, List<Integer> quantities, Authentication authentication) {
        if (itemIds.size() != quantities.size()) {
            throw new BadRequestException("Item IDs and quantities lists must have the same size");
        }

        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);
            reduceInventoryQuantity(itemId, quantity, authentication);
        }
    }

}
