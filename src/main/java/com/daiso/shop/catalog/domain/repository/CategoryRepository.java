package com.daiso.shop.catalog.domain.repository;

import com.daiso.shop.catalog.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByCategoryCode(String categoryCode);

    List<CategoryEntity> findByParentIdIsNull();
    List<CategoryEntity> findByParentIdIsNullAndActiveTrue();

    List<CategoryEntity> findByParentId(Long parentId);
    List<CategoryEntity> findByParentIdAndActiveTrue(Long parentId);
}