package com.panoramic.dto.inventory;

import lombok.Data;

@Data
public class InventoryStatsResponse {
    private long totalItems;
    private long lowStockItems;
    private double valueInStock;
    private long outOfStockItems;
    private long inStockItems;
}
