package com.daiso.shop.category.application.query.dto;

public record ProductView(
        Long productId,
        String productCode,
        String productName,
        Long categoryId,
        String categoryCode,
        String categoryName,
        boolean isActive
) {}