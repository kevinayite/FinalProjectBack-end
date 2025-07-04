package com.panoramic.dto.inventory;

import com.panoramic.model.InventoryItem;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Base64;

@Data
public class InventoryItemResponse {
    private Long id;
    private String itemName;
    private String skuNumber;
    private String category;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer reorderPoint;
    private String stockStatus;
    private Boolean isActive;
    private String itemImageBase64; // keep this for request


    // Add other fields if needed (like base64 image, createdAt, etc.)

    public InventoryItemResponse(InventoryItem item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.skuNumber = item.getSkuNumber();
        this.category = item.getCategory();
        this.description = item.getDescription();
        this.quantity = item.getQuantity();
        this.unitPrice = item.getUnitPrice();
        this.reorderPoint = item.getReorderPoint();

        if (item.getItemImage() != null) {
            this.itemImageBase64 = Base64.getEncoder().encodeToString(item.getItemImage());
        }
        this.stockStatus = computeStockStatus(item.getQuantity(), item.getReorderPoint());
    }

    private String computeStockStatus(Integer quantity, Integer reorderPoint) {
        if (quantity == null || reorderPoint == null) return "Unknown";
        if (quantity == 0) return "Out of Stock";
        if (quantity <= reorderPoint) return "Low Stock";
        return "In Stock";
    }
}
