package com.daiso.shop.catalog.domain.repository;

import com.daiso.shop.catalog.domain.entity.CategoryEntity;
import com.daiso.shop.catalog.domain.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductCode(String productCode);

    Page<ProductEntity> findByCategory(CategoryEntity category, Pageable pageable);
    Page<ProductEntity> findByCategoryAndActiveTrue(CategoryEntity category, Pageable pageable);

    Page<ProductEntity> findByActiveTrue(Pageable pageable);
}