package com.daiso.shop.catalog.domain.entity; // <- 패키지는 보통 product 쪽으로 분리 권장

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

    // ---- getters ----
    public Long getProductId() { return productId; }
    public String getProductCode() { return productCode; }
    public String getProductName() { return productName; }

    public CategoryEntity getCategory() { return category; }

    public boolean isActive() { return active; }
}