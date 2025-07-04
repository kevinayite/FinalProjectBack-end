package com.panoramic.service;

import com.panoramic.dto.inventory.InventoryItemRequest;
import com.panoramic.dto.inventory.InventoryItemResponse;
import com.panoramic.dto.inventory.InventoryStatsResponse;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.InventoryItem;
import com.panoramic.model.User;
import com.panoramic.repository.InventoryItemRepository;
import com.panoramic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public InventoryItem createInventoryItem(InventoryItemRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);

        // Check if SKU already exists
        if (request.getSkuNumber() != null && inventoryItemRepository.existsBySkuNumber(request.getSkuNumber())) {
            throw new BadRequestException("SKU number already exists");
        }

        InventoryItem item = new InventoryItem();
        item.setItemName(request.getItemName());
        item.setSkuNumber(request.getSkuNumber());
        item.setCategory(request.getCategory());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        item.setReorderPoint(request.getReorderPoint());
//        item.setItemImage(request.getItemImage().getBytes());
        item.setItemImage(decodeBase64Image(request.getItemImageBase64()));

        item.setUser(user);
        item.setIsActive(true);

        return inventoryItemRepository.save(item);
    }
    @Transactional
    public Page<InventoryItem> getInventoryItemsInternal(Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        return inventoryItemRepository.findByUserId(user.getId(), pageable);
    }

    public InventoryItem getInventoryItemByIdInternal(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
        
        // Check if item belongs to current user
        if (!item.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Inventory item not found");
        }
        
        return item;
    }

    @Transactional
    public InventoryItem updateInventoryItem(Long id, InventoryItemRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
        
        // Check if item belongs to current user
        if (!item.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Inventory item not found");
        }

        // Check if SKU already exists (excluding current item)
        if (request.getSkuNumber() != null && 
            !request.getSkuNumber().equals(item.getSkuNumber()) && 
            inventoryItemRepository.existsBySkuNumber(request.getSkuNumber())) {
            throw new BadRequestException("SKU number already exists");
        }

        item.setItemName(request.getItemName());
        item.setSkuNumber(request.getSkuNumber());
        item.setCategory(request.getCategory());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        item.setReorderPoint(request.getReorderPoint());
//        item.setItemImage(request.getItemImage().getBytes());
        item.setItemImage(decodeBase64Image(request.getItemImageBase64()));


        return inventoryItemRepository.save(item);
    }

    @Transactional
    public void deleteInventoryItem(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
        
        // Check if item belongs to current user
        if (!item.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Inventory item not found");
        }
        
        item.setIsActive(false);
        inventoryItemRepository.save(item);
    }

    public Page<InventoryItem> searchInventoryItems(String search, Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        return inventoryItemRepository.findByUserIdAndSearch(user.getId(), search, pageable);
    }

    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public InventoryItemResponse getInventoryItemById(Long id, Authentication authentication) {
        InventoryItem item = getInventoryItemByIdInternal(id, authentication);
        return new InventoryItemResponse(item);
    }
    public Page<InventoryItemResponse> getInventoryItems(Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        Page<InventoryItem> items = inventoryItemRepository.findByUserId(user.getId(), pageable);
        return items.map(InventoryItemResponse::new); // Map each entity to response
    }
    private byte[] decodeBase64Image(String base64Image) {
        return base64Image != null && !base64Image.isBlank() ? Base64.getDecoder().decode(base64Image) : null;
    }

//    public InventoryStatsResponse getInventoryStats(Authentication authentication) {
//        // Example logic, you can refine this
//        long totalItems = inventoryItemRepository.countByIsActiveTrue();
//        long lowStockItems = inventoryItemRepository.countLowStockItems();
//        double valueInStock = inventoryItemRepository.calculateTotalStockValue();
//
//        InventoryStatsResponse stats = new InventoryStatsResponse();
//        stats.setTotalItems(totalItems);
//        stats.setLowStockItems(lowStockItems);
//        stats.setValueInStock(valueInStock);
//
//        return stats;
//    }

    public InventoryStatsResponse getInventoryStats(Authentication authentication) {
        long totalItems = inventoryItemRepository.countByIsActiveTrue();
        long lowStockItems = inventoryItemRepository.countLowStockItems();
        long outOfStockItems = inventoryItemRepository.countOutOfStockItems();
        long inStockItems = inventoryItemRepository.countInStockItems();
        double valueInStock = inventoryItemRepository.calculateTotalStockValue();

        InventoryStatsResponse stats = new InventoryStatsResponse();
        stats.setTotalItems(totalItems);
        stats.setLowStockItems(lowStockItems);
        stats.setOutOfStockItems(outOfStockItems);
        stats.setInStockItems(inStockItems);
        stats.setValueInStock(valueInStock);

        return stats;
    }
    public List<InventoryItemResponse> getAvailableInventoryItems(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<InventoryItem> items = inventoryItemRepository.findByUserIdAndIsActiveTrue(user.getId())
                .stream()
                .filter(item -> item.getQuantity() > 0)
                .toList();

        return items.stream()
                .map(InventoryItemResponse::new)
                .toList();
    }




}
