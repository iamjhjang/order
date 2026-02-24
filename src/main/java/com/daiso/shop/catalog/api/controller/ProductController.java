package com.daiso.shop.catalog.api.controller;

import com.daiso.shop.catalog.api.dto.ProductUpsertRequest;
import com.daiso.shop.catalog.application.query.ProductService;
import com.daiso.shop.catalog.application.query.dto.ProductView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 단건 조회 (code)
    // 예) /api/products/P0001
    @GetMapping("/products/{productCode}")
    public ProductView getByCode(@PathVariable String productCode) {
        return productService.getByCode(productCode);
    }

    // 전체 상품 목록(페이징)
    // 예) /api/products?activeOnly=true&page=0&size=20
    @GetMapping("/products")
    public Page<ProductView> listAll(
            @RequestParam(defaultValue = "true") boolean activeOnly,
            Pageable pageable
    ) {
        return productService.listAll(activeOnly, pageable);
    }

    // 카테고리별 상품 목록(페이징) - URL에 categoryCode 사용
    // 예) /api/categories/FOOD/products?activeOnly=true&page=0&size=20
    @GetMapping("/categories/{categoryCode}/products")
    public Page<ProductView> listByCategory(
            @PathVariable String categoryCode,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            Pageable pageable
    ) {
        return productService.listByCategoryCode(categoryCode, activeOnly, pageable);
    }


    @PostMapping("/products")
    public ProductView create(@RequestBody ProductUpsertRequest request) {
        return productService.create(
                request.productCode(),
                request.productName(),
                request.categoryCode(),
                request.active()
        );
    }

    @PutMapping("/products/{productCode}")
    public ProductView update(
            @PathVariable String productCode,
            @RequestBody ProductUpsertRequest request
    ) {
        return productService.update(
                productCode,
                request.productName(),
                request.categoryCode(),
                request.active()
        );
    }
}