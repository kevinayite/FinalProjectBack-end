package com.panoramic.controller;

import com.panoramic.dto.inventory.InventoryItemRequest;
import com.panoramic.dto.inventory.InventoryItemResponse;
import com.panoramic.dto.inventory.InventoryStatsResponse;
import com.panoramic.model.InventoryItem;
import com.panoramic.service.InventoryService;
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

    @PostMapping
    public ResponseEntity<InventoryItem> createInventoryItem(@Valid @RequestBody InventoryItemRequest request,
                                                           Authentication authentication) {
        InventoryItem item = inventoryService.createInventoryItem(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping
    public ResponseEntity<Page<InventoryItem>> getInventoryItems(Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryService.getInventoryItemsInternal(authentication, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id, Authentication authentication) {
        InventoryItem item = inventoryService.getInventoryItemByIdInternal(id, authentication);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id,
                                                           @Valid @RequestBody InventoryItemRequest request,
                                                           Authentication authentication) {
        InventoryItem item = inventoryService.updateInventoryItem(id, request, authentication);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id, Authentication authentication) {
        inventoryService.deleteInventoryItem(id, authentication);
        return ResponseEntity.noContent().build();
    }

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

    @GetMapping("/stats")
    public ResponseEntity<InventoryStatsResponse> getInventoryStats(Authentication authentication) {
        InventoryStatsResponse stats = inventoryService.getInventoryStats(authentication);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/available")
    public ResponseEntity<List<InventoryItemResponse>> getAvailableInventoryItems(Authentication authentication) {
        List<InventoryItemResponse> items = inventoryService.getAvailableInventoryItems(authentication);
        return ResponseEntity.ok(items);
    }

}
