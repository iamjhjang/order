package com.daiso.shop.catalog.api.dto;

public record ProductUpsertRequest(
        String productCode,
        String productName,
        String categoryCode,
        boolean active
) {}
