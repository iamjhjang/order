package com.daiso.shop.category.domain.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 대형 서비스에서 자주 쓰는 패턴:
     * - parent_id만 필요한 경우가 많아서 parentId를 필드로 두고
     * - parent 엔티티는 필요할 때만 LAZY 로딩 (insertable/updatable=false)
     */
    @Column(name = "category_parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_id", insertable = false, updatable = false)
    private CategoryEntity parent;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "sort_order")
    private int sortOrder = 0;

    @CreatedBy
    @Column(name = "create_id", updatable = false)
    private String createId;

    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    private LocalDateTime createDt;

    @LastModifiedBy
    @Column(name = "update_id")
    private String updateId;

    @LastModifiedDate
    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    @Version
    @Column(name = "version")
    private Long version;

    protected CategoryEntity() {}

    // 보통은 생성 규칙을 강제하려고 생성 메서드/생성자에 필수값을 받습니다.
    public static CategoryEntity create(String categoryCode, String categoryName, Long parentId, int sortOrder) {
        CategoryEntity e = new CategoryEntity();
        e.categoryCode = categoryCode;
        e.categoryName = categoryName;
        e.parentId = parentId;
        e.sortOrder = sortOrder;
        e.active = true;
        return e;
    }

    // ---- 도메인 변경 메서드(Setter 최소화) ----
    public void rename(String categoryName) {
        this.categoryName = categoryName;
    }

    public void moveTo(Long parentId) {
        this.parentId = parentId;
    }

    public void changeSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    // ---- getters ----
    public Long getCategoryId() { return categoryId; }
    public Long getParentId() { return parentId; }
    public CategoryEntity getParent() { return parent; }

    public String getCategoryCode() { return categoryCode; }
    public String getCategoryName() { return categoryName; }

    public boolean isActive() { return active; }
    public int getSortOrder() { return sortOrder; }

    public String getCreateId() { return createId; }
    public LocalDateTime getCreateDt() { return createDt; }

    public String getUpdateId() { return updateId; }
    public LocalDateTime getUpdateDt() { return updateDt; }
}