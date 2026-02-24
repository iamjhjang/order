package com.daiso.shop.catalog.api.controller;

import com.daiso.shop.catalog.application.query.CategoryService;
import com.daiso.shop.catalog.application.query.dto.CategoryView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 카테고리 단건 조회 (code)
    @GetMapping("/{categoryCode}")
    public CategoryView getByCode(@PathVariable String categoryCode) {
        return categoryService.getByCode(categoryCode);
    }

    // 루트 카테고리 목록
    // 예) /api/categories?activeOnly=true
    @GetMapping
    public List<CategoryView> listRoot(
            @RequestParam(defaultValue = "true") boolean activeOnly
    ) {
        return categoryService.listRoot(activeOnly);
    }

    // 특정 카테고리의 자식 목록
    // 예) /api/categories/FOOD/children?activeOnly=true
    @GetMapping("/{parentCategoryCode}/children")
    public List<CategoryView> listChildren(
            @PathVariable String parentCategoryCode,
            @RequestParam(defaultValue = "true") boolean activeOnly
    ) {
        return categoryService.listChildren(parentCategoryCode, activeOnly);
    }
}