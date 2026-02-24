package com.daiso.shop.category.application.query;

import com.daiso.shop.category.application.query.dto.ProductView;
import com.daiso.shop.category.domain.entity.CategoryEntity;
import com.daiso.shop.category.domain.entity.ProductEntity;
import com.daiso.shop.category.domain.repository.CategoryRepository;
import com.daiso.shop.category.domain.repository.ProductRepository;
import com.daiso.shop.common.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

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
        CategoryEntity c = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new NotFoundException("Category not found. code=" + categoryCode));

        Page<ProductEntity> page = activeOnly
                ? productRepository.findByCategoryAndIsActiveTrue(c, pageable)
                : productRepository.findByCategory(c, pageable);

        return page.map(this::toView);
    }

    public Page<ProductView> listAll(boolean activeOnly, Pageable pageable) {
        Page<ProductEntity> page = activeOnly
                ? productRepository.findByIsActiveTrue(pageable)
                : productRepository.findAll(pageable);
        return page.map(this::toView);
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