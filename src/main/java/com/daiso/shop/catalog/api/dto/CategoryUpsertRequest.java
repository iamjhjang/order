package com.daiso.shop.catalog.api.dto;

public record CategoryUpsertRequest(
        String categoryCode,
        String categoryName,
        String parentCategoryCode,
        Integer sortOrder,
        boolean active
) {}