package com.daiso.shop.catalog.application.query;

import com.daiso.shop.catalog.application.query.dto.ProductView;
import com.daiso.shop.catalog.domain.entity.CategoryEntity;
import com.daiso.shop.catalog.domain.entity.ProductEntity;
import com.daiso.shop.catalog.domain.repository.CategoryRepository;
import com.daiso.shop.catalog.domain.repository.ProductRepository;
import com.daiso.shop.common.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of(
            "productId",
            "productCode",
            "productName",
            "categoryId",
            "active",
            "createDt",
            "updateDt"
    );

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductView getByCode(String productCode) {
        ProductEntity p = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Product not found. code=" + productCode));
        return toView(p);
    }

    // URL에 categoryCode 쓰는 방식(네가 원한 방식)
    public Page<ProductView> listByCategoryCode(String categoryCode, boolean activeOnly, Pageable pageable) {
        Pageable safePageable = sanitizePageable(pageable);
        CategoryEntity c = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new NotFoundException("Category not found. code=" + categoryCode));

        Page<ProductEntity> page = activeOnly
                ? productRepository.findByCategoryAndActiveTrue(c, safePageable)
                : productRepository.findByCategory(c, safePageable);

        return page.map(this::toView);
    }

    public Page<ProductView> listAll(boolean activeOnly, Pageable pageable) {
        Pageable safePageable = sanitizePageable(pageable);
        Page<ProductEntity> page = activeOnly
                ? productRepository.findByActiveTrue(safePageable)
                : productRepository.findAll(safePageable);
        return page.map(this::toView);
    }

    private Pageable sanitizePageable(Pageable pageable) {
        Sort safeSort = pageable.getSort().stream()
                .filter(order -> ALLOWED_SORT_PROPERTIES.contains(order.getProperty()))
                .collect(Sort::by, Sort::and, Sort::and);

        if (safeSort.isUnsorted()) {
            safeSort = Sort.by(Sort.Order.asc("productId"));
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), safeSort);
    }

    private ProductView toView(ProductEntity p) {
        CategoryEntity c = p.getCategory();
        return new ProductView(
                p.getProductId(),
                p.getProductCode(),
                p.getProductName(),
                c == null ? null : c.getCategoryId(),
                c == null ? null : c.getCategoryCode(),
                c == null ? null : c.getCategoryName(),
                Boolean.TRUE.equals(p.isActive())
        );
    }
}