//package com.panoramic.service;
//
//import com.panoramic.dto.inventory.InventoryItemRequest;
//import com.panoramic.dto.inventory.InventoryItemResponse;
//import com.panoramic.dto.inventory.InventoryStatsResponse;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.InventoryItem;
//import com.panoramic.model.User;
//import com.panoramic.repository.InventoryItemRepository;
//import com.panoramic.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Base64;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InventoryService {
//
//    private final InventoryItemRepository inventoryItemRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public InventoryItem createInventoryItem(InventoryItemRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        // Check if SKU already exists
//        if (request.getSkuNumber() != null && inventoryItemRepository.existsBySkuNumber(request.getSkuNumber())) {
//            throw new BadRequestException("SKU number already exists");
//        }
//
//        InventoryItem item = new InventoryItem();
//        item.setItemName(request.getItemName());
//        item.setSkuNumber(request.getSkuNumber());
//        item.setCategory(request.getCategory());
//        item.setDescription(request.getDescription());
//        item.setQuantity(request.getQuantity());
//        item.setUnitPrice(request.getUnitPrice());
//        item.setReorderPoint(request.getReorderPoint());
////        item.setItemImage(request.getItemImage().getBytes());
//        item.setItemImage(decodeBase64Image(request.getItemImageBase64()));
//
//        item.setUser(user);
//        item.setIsActive(true);
//
//        return inventoryItemRepository.save(item);
//    }
//    @Transactional
//    public Page<InventoryItem> getInventoryItemsInternal(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        return inventoryItemRepository.findByUserId(user.getId(), pageable);
//    }
//
//    public InventoryItem getInventoryItemByIdInternal(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        InventoryItem item = inventoryItemRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
//
//        // Check if item belongs to current user
//        if (!item.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Inventory item not found");
//        }
//
//        return item;
//    }
//
//    @Transactional
//    public InventoryItem updateInventoryItem(Long id, InventoryItemRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        InventoryItem item = inventoryItemRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
//
//        // Check if item belongs to current user
//        if (!item.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Inventory item not found");
//        }
//
//        // Check if SKU already exists (excluding current item)
//        if (request.getSkuNumber() != null &&
//            !request.getSkuNumber().equals(item.getSkuNumber()) &&
//            inventoryItemRepository.existsBySkuNumber(request.getSkuNumber())) {
//            throw new BadRequestException("SKU number already exists");
//        }
//
//        item.setItemName(request.getItemName());
//        item.setSkuNumber(request.getSkuNumber());
//        item.setCategory(request.getCategory());
//        item.setDescription(request.getDescription());
//        item.setQuantity(request.getQuantity());
//        item.setUnitPrice(request.getUnitPrice());
//        item.setReorderPoint(request.getReorderPoint());
////        item.setItemImage(request.getItemImage().getBytes());
//        item.setItemImage(decodeBase64Image(request.getItemImageBase64()));
//
//
//        return inventoryItemRepository.save(item);
//    }
//
//    @Transactional
//    public void deleteInventoryItem(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        InventoryItem item = inventoryItemRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
//
//        // Check if item belongs to current user
//        if (!item.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Inventory item not found");
//        }
//
//        item.setIsActive(false);
//        inventoryItemRepository.save(item);
//    }
//
//    public Page<InventoryItem> searchInventoryItems(String search, Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        return inventoryItemRepository.findByUserIdAndSearch(user.getId(), search, pageable);
//    }
//
//    public List<InventoryItem> getLowStockItems() {
//        return inventoryItemRepository.findLowStockItems();
//    }
//
//    private User getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//
//    public InventoryItemResponse getInventoryItemById(Long id, Authentication authentication) {
//        InventoryItem item = getInventoryItemByIdInternal(id, authentication);
//        return new InventoryItemResponse(item);
//    }
//    public Page<InventoryItemResponse> getInventoryItems(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        Page<InventoryItem> items = inventoryItemRepository.findByUserId(user.getId(), pageable);
//        return items.map(InventoryItemResponse::new); // Map each entity to response
//    }
//    private byte[] decodeBase64Image(String base64Image) {
//        return base64Image != null && !base64Image.isBlank() ? Base64.getDecoder().decode(base64Image) : null;
//    }
//
////    public InventoryStatsResponse getInventoryStats(Authentication authentication) {
////        // Example logic, you can refine this
////        long totalItems = inventoryItemRepository.countByIsActiveTrue();
////        long lowStockItems = inventoryItemRepository.countLowStockItems();
////        double valueInStock = inventoryItemRepository.calculateTotalStockValue();
////
////        InventoryStatsResponse stats = new InventoryStatsResponse();
////        stats.setTotalItems(totalItems);
////        stats.setLowStockItems(lowStockItems);
////        stats.setValueInStock(valueInStock);
////
////        return stats;
////    }
//
//    public InventoryStatsResponse getInventoryStats(Authentication authentication) {
//        long totalItems = inventoryItemRepository.countByIsActiveTrue();
//        long lowStockItems = inventoryItemRepository.countLowStockItems();
//        long outOfStockItems = inventoryItemRepository.countOutOfStockItems();
//        long inStockItems = inventoryItemRepository.countInStockItems();
//        double valueInStock = inventoryItemRepository.calculateTotalStockValue();
//
//        InventoryStatsResponse stats = new InventoryStatsResponse();
//        stats.setTotalItems(totalItems);
//        stats.setLowStockItems(lowStockItems);
//        stats.setOutOfStockItems(outOfStockItems);
//        stats.setInStockItems(inStockItems);
//        stats.setValueInStock(valueInStock);
//
//        return stats;
//    }
//    public List<InventoryItemResponse> getAvailableInventoryItems(Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        List<InventoryItem> items = inventoryItemRepository.findByUserIdAndIsActiveTrue(user.getId())
//                .stream()
//                .filter(item -> item.getQuantity() > 0)
//                .toList();
//
//        return items.stream()
//                .map(InventoryItemResponse::new)
//                .toList();
//    }
//
//
//
//
//}






package com.panoramic.service;

import com.panoramic.dto.inventory.InventoryItemRequest;
import com.panoramic.dto.inventory.InventoryItemResponse;
import com.panoramic.dto.inventory.InventoryStatsResponse;
import com.panoramic.dto.invoice.InvoiceItemRequest;
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

import java.util.ArrayList;
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
        item.setItemImage(decodeBase64Image(request.getItemImageBase64()));
        item.setUser(user); // Still track who created it, but don't filter by it
        item.setIsActive(true);

        return inventoryItemRepository.save(item);
    }

    // Updated: Get all inventory items (removed user filtering)
    @Transactional
    public Page<InventoryItem> getInventoryItemsInternal(Authentication authentication, Pageable pageable) {
        // Authentication required but no user filtering
        return inventoryItemRepository.findAllActiveItems(pageable);
    }

    // Updated: Get any inventory item by ID (removed user ownership check)
    public InventoryItem getInventoryItemByIdInternal(Long id, Authentication authentication) {
        // Authentication required but no user ownership check
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
    }

    // Updated: Any authenticated user can update any inventory item
    @Transactional
    public InventoryItem updateInventoryItem(Long id, InventoryItemRequest request, Authentication authentication) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));

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
        item.setItemImage(decodeBase64Image(request.getItemImageBase64()));

        return inventoryItemRepository.save(item);
    }

    // Updated: Any authenticated user can delete any inventory item
    @Transactional
    public void deleteInventoryItem(Long id, Authentication authentication) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));

        item.setIsActive(false);
        inventoryItemRepository.save(item);
    }

    // Updated: Search across all inventory items (removed user filtering)
    public Page<InventoryItem> searchInventoryItems(String search, Authentication authentication, Pageable pageable) {
        return inventoryItemRepository.findBySearch(search, pageable);
    }

    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Updated: Get any inventory item by ID (removed user filtering)
    public InventoryItemResponse getInventoryItemById(Long id, Authentication authentication) {
        InventoryItem item = getInventoryItemByIdInternal(id, authentication);
        return new InventoryItemResponse(item);
    }

    // Updated: Get all inventory items (removed user filtering)
    public Page<InventoryItemResponse> getInventoryItems(Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findAllActiveItems(pageable);
        return items.map(InventoryItemResponse::new);
    }

    private byte[] decodeBase64Image(String base64Image) {
        return base64Image != null && !base64Image.isBlank() ? Base64.getDecoder().decode(base64Image) : null;
    }

    // Updated: Get global inventory stats (already working globally)
    public InventoryStatsResponse getInventoryStats(Authentication authentication) {
        long totalItems = inventoryItemRepository.countByIsActiveTrue();
        long lowStockItems = inventoryItemRepository.countLowStockItems();
        long outOfStockItems = inventoryItemRepository.countOutOfStockItems();
        long inStockItems = inventoryItemRepository.countInStockItems();
        Double valueInStock = inventoryItemRepository.calculateTotalStockValue();

        // Handle null value from database
        double actualValueInStock = valueInStock != null ? valueInStock : 0.0;

        InventoryStatsResponse stats = new InventoryStatsResponse();
        stats.setTotalItems(totalItems);
        stats.setLowStockItems(lowStockItems);
        stats.setOutOfStockItems(outOfStockItems);
        stats.setInStockItems(inStockItems);
        stats.setValueInStock(actualValueInStock);

        return stats;
    }

    // Updated: Get all available inventory items (removed user filtering)
    public List<InventoryItemResponse> getAvailableInventoryItems(Authentication authentication) {
        List<InventoryItem> items = inventoryItemRepository.findAvailableItems();
        return items.stream()
                .map(InventoryItemResponse::new)
                .toList();
    }

    // Additional methods for enhanced functionality
    public Page<InventoryItemResponse> getInventoryItemsByCategory(String category, Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findByCategory(category, pageable);
        return items.map(InventoryItemResponse::new);
    }

    public List<String> getAllCategories(Authentication authentication) {
        return inventoryItemRepository.findAllCategories();
    }

    // Method to get all items including inactive ones
    public Page<InventoryItemResponse> getAllInventoryItems(Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findAllItems(pageable);
        return items.map(InventoryItemResponse::new);
    }

    // Method to update only quantity (useful for stock adjustments)
    @Transactional
    public InventoryItem updateQuantity(Long id, Integer newQuantity, Authentication authentication) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));

        if (newQuantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        item.setQuantity(newQuantity);
        return inventoryItemRepository.save(item);
    }

    // Add these methods to the existing InventoryService class

    // Get out of stock items
    public Page<InventoryItemResponse> getOutOfStockItems(Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findOutOfStockItems(pageable);
        return items.map(InventoryItemResponse::new);
    }

    // Get in stock items
    public Page<InventoryItemResponse> getInStockItems(Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findInStockItems(pageable);
        return items.map(InventoryItemResponse::new);
    }

    // Bulk update quantities
    @Transactional
    public List<InventoryItem> bulkUpdateQuantities(List<Long> itemIds, List<Integer> quantities, Authentication authentication) {
        if (itemIds.size() != quantities.size()) {
            throw new BadRequestException("Item IDs and quantities lists must have the same size");
        }

        List<InventoryItem> updatedItems = new ArrayList<>();
        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);

            InventoryItem item = inventoryItemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + itemId));

            if (quantity < 0) {
                throw new BadRequestException("Quantity cannot be negative for item ID: " + itemId);
            }

            item.setQuantity(quantity);
            updatedItems.add(inventoryItemRepository.save(item));
        }

        return updatedItems;
    }

    // Get items by price range
    public Page<InventoryItemResponse> getItemsByPriceRange(Double minPrice, Double maxPrice, Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return items.map(InventoryItemResponse::new);
    }

    // Get items by quantity range
    public Page<InventoryItemResponse> getItemsByQuantityRange(Integer minQuantity, Integer maxQuantity, Authentication authentication, Pageable pageable) {
        Page<InventoryItem> items = inventoryItemRepository.findByQuantityRange(minQuantity, maxQuantity, pageable);
        return items.map(InventoryItemResponse::new);
    }








    // Add these methods to your existing InventoryService class

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
            if (itemId != null && quantity != null && quantity > 0) {
                reduceInventoryQuantity(itemId, quantity, authentication);
            }
        }
    }

    @Transactional
    public void restoreInventoryQuantity(Long inventoryItemId, Integer quantityToRestore, Authentication authentication) {
        InventoryItem item = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + inventoryItemId));

        item.setQuantity(item.getQuantity() + quantityToRestore);
        inventoryItemRepository.save(item);
    }

    @Transactional
    public void bulkRestoreInventoryQuantities(List<Long> itemIds, List<Integer> quantities, Authentication authentication) {
        if (itemIds.size() != quantities.size()) {
            throw new BadRequestException("Item IDs and quantities lists must have the same size");
        }

        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);
            if (itemId != null && quantity != null && quantity > 0) {
                restoreInventoryQuantity(itemId, quantity, authentication);
            }
        }
    }

    // Method to check stock availability before creating invoice
    public void validateStockAvailability(List<InvoiceItemRequest> items) {
        for (InvoiceItemRequest item : items) {
            if (item.getInventoryItemId() != null) {
                InventoryItem inventoryItem = inventoryItemRepository.findById(item.getInventoryItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + item.getInventoryItemId()));

                if (inventoryItem.getQuantity() < item.getQuantity()) {
                    throw new BadRequestException("Insufficient stock for item: " + inventoryItem.getItemName() +
                            ". Available: " + inventoryItem.getQuantity() + ", Required: " + item.getQuantity());
                }
            }
        }
    }


}
