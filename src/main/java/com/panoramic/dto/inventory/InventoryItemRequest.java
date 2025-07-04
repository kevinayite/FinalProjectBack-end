package com.panoramic.dto.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryItemRequest {
    @NotBlank(message = "Item name is required")
    private String itemName;

    private String skuNumber;

    @NotBlank(message = "Category is required")
    private String category;

    private String description;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;

    private Integer reorderPoint;
    private String itemImageBase64; // keep this for request


}
