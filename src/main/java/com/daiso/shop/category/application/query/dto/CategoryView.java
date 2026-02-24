package com.daiso.shop.category.application.query.dto;

public record CategoryView(
        Long categoryId,
        Long parentCategoryId,
        String categoryCode,
        String categoryName,
        boolean isActive,
        int sortOrder
) {}