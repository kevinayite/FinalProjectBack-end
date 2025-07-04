package com.panoramic.dto.supplier;

public record SupplierStatsResponse(
        long totalSuppliers,
        long activeSuppliers,
        long inactiveSuppliers,
        long categoriesCount
) {}
