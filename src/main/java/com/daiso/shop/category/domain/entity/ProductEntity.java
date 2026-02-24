package com.daiso.shop.category.domain.entity; // <- 패키지는 보통 product 쪽으로 분리 권장

import com.daiso.shop.category.domain.entity.CategoryEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    // 자주 쓰는 패턴: FK id를 필드로 들고, 엔티티 참조는 읽을 때만
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "is_active")
    private boolean active = true;

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

    protected ProductEntity() {}

    public static ProductEntity create(String productCode, String productName, Long categoryId) {
        ProductEntity e = new ProductEntity();
        e.productCode = productCode;
        e.productName = productName;
        e.categoryId = categoryId;
        e.active = true;
        return e;
    }

    // ---- 도메인 변경 메서드(Setter 최소화) ----
    public void rename(String productName) {
        this.productName = productName;
    }

    public void changeCategory(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void activate() { this.active = true; }
    public void deactivate() { this.active = false; }

    // ---- getters ----
    public Long getProductId() { return productId; }
    public String getProductCode() { return productCode; }
    public String getProductName() { return productName; }

    public Long getCategoryId() { return categoryId; }
    public CategoryEntity getCategory() { return category; }

    public boolean isActive() { return active; }

    public String getCreateId() { return createId; }
    public LocalDateTime getCreateDt() { return createDt; }
    public String getUpdateId() { return updateId; }
    public LocalDateTime getUpdateDt() { return updateDt; }
}