CREATE TABLE product (
                         product_id    BIGINT NOT NULL AUTO_INCREMENT,
                         product_code  VARCHAR(50)  NOT NULL,
                         product_name  VARCHAR(200) NOT NULL,
                         category_id   BIGINT       NOT NULL,
                         is_active     TINYINT(1)   NOT NULL DEFAULT 1,
                         create_id     VARCHAR(50)  NULL,
                         create_dt     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                         update_id     VARCHAR(50)  NULL,
                         update_dt     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                         version       BIGINT       NOT NULL DEFAULT 0,
                         PRIMARY KEY (product_id),
                         CONSTRAINT uq_product_code UNIQUE (product_code),
                         CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(category_id)
) ENGINE=InnoDB;

CREATE INDEX idx_product_category_active ON product(category_id, is_active);