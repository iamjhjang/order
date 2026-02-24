package com.daiso.shop.catalog.application.query;

import com.daiso.shop.catalog.application.query.dto.CategoryView;
import com.daiso.shop.catalog.domain.entity.CategoryEntity;
import com.daiso.shop.catalog.domain.repository.CategoryRepository;
import com.daiso.shop.common.NotFoundException;
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