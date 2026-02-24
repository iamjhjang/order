package com.daiso.shop.catalog.application.query;

import com.daiso.shop.catalog.application.query.dto.CategoryView;
import com.daiso.shop.catalog.domain.entity.CategoryEntity;
import com.daiso.shop.catalog.domain.repository.CategoryRepository;
import com.daiso.shop.common.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryView getByCode(String categoryCode) {
        CategoryEntity c = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new NotFoundException("Category not found. code=" + categoryCode));
        return toView(c);
    }

    public List<CategoryView> listRoot(boolean activeOnly) {
        List<CategoryEntity> list = activeOnly
                ? categoryRepository.findByParentIdIsNullAndActiveTrue()
                : categoryRepository.findByParentIdIsNull();

        return list.stream().map(this::toView).toList();
    }

    public List<CategoryView> listChildren(String parentCategoryCode, boolean activeOnly) {
        CategoryEntity parent = categoryRepository.findByCategoryCode(parentCategoryCode)
                .orElseThrow(() -> new NotFoundException("Parent category not found. code=" + parentCategoryCode));

        Long parentId = parent.getCategoryId();

        List<CategoryEntity> list = activeOnly
                ? categoryRepository.findByParentIdAndActiveTrue(parentId)
                : categoryRepository.findByParentId(parentId);

        return list.stream().map(this::toView).toList();
    }

    public List<CategoryView> listAll(boolean activeOnly) {
        Sort sort = Sort.by(Sort.Order.asc("sortOrder"), Sort.Order.asc("categoryId"));
        List<CategoryEntity> list = activeOnly
                ? categoryRepository.findByActiveTrue(sort)
                : categoryRepository.findAll(sort);
        return list.stream().map(this::toView).toList();
    }

    @Transactional
    public CategoryView create(String categoryCode, String categoryName, String parentCategoryCode, Integer sortOrder, boolean active) {
        if (categoryCode == null || categoryCode.isBlank() || categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("카테고리 코드와 이름은 필수입니다.");
        }

        if (categoryRepository.findByCategoryCode(categoryCode).isPresent()) {
            throw new IllegalArgumentException("Category code already exists. code=" + categoryCode);
        }

        Long parentId = resolveParentId(parentCategoryCode);
        int safeSortOrder = sortOrder == null ? 0 : sortOrder;

        CategoryEntity category = CategoryEntity.create(categoryCode, categoryName, parentId, safeSortOrder);
        if (!active) {
            category.deactivate();
        }

        return toView(categoryRepository.save(category));
    }

    @Transactional
    public CategoryView update(String categoryCode, String categoryName, String parentCategoryCode, Integer sortOrder, boolean active) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("카테고리 이름은 필수입니다.");
        }

        CategoryEntity category = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new NotFoundException("Category not found. code=" + categoryCode));

        Long parentId = resolveParentId(parentCategoryCode);
        if (parentId != null && parentId.equals(category.getCategoryId())) {
            throw new IllegalArgumentException("Category cannot be its own parent. code=" + categoryCode);
        }

        int safeSortOrder = sortOrder == null ? category.getSortOrder() : sortOrder;

        category.rename(categoryName);
        category.moveTo(parentId);
        category.changeSortOrder(safeSortOrder);
        if (active) {
            category.activate();
        } else {
            category.deactivate();
        }

        return toView(category);
    }

    @Transactional
    public void delete(String categoryCode) {
        CategoryEntity category = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new NotFoundException("Category not found. code=" + categoryCode));

        try {
            categoryRepository.delete(category);
            categoryRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("하위 카테고리 또는 상품이 연결된 카테고리는 삭제할 수 없습니다. code=" + categoryCode);
        }
    }

    private Long resolveParentId(String parentCategoryCode) {
        if (parentCategoryCode == null || parentCategoryCode.isBlank()) {
            return null;
        }

        CategoryEntity parent = categoryRepository.findByCategoryCode(parentCategoryCode)
                .orElseThrow(() -> new NotFoundException("Parent category not found. code=" + parentCategoryCode));
        return parent.getCategoryId();
    }


    private CategoryView toView(CategoryEntity c) {
        return new CategoryView(
                c.getCategoryId(),
                c.getParentId(),        // parent 엔티티 로딩 없이 바로
                c.getCategoryCode(),
                c.getCategoryName(),
                c.isActive(),           // boolean
                c.getSortOrder()        // int (null 체크 불필요)
        );
    }
}