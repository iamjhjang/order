CREATE TABLE category (
                          category_id        BIGINT NOT NULL AUTO_INCREMENT,
                          category_parent_id BIGINT NULL,
                          category_code      VARCHAR(50)  NOT NULL,
                          category_name      VARCHAR(200) NOT NULL,
                          is_active          TINYINT(1)   NOT NULL DEFAULT 1,
                          sort_order         INT          NOT NULL DEFAULT 0,
                          create_id          VARCHAR(50)  NULL,
                          create_dt          DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                          update_id          VARCHAR(50)  NULL,
                          update_dt          DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                          version            BIGINT       NOT NULL DEFAULT 0,
                          PRIMARY KEY (category_id),
                          CONSTRAINT uq_category_code UNIQUE (category_code),
                          CONSTRAINT fk_category_parent FOREIGN KEY (category_parent_id) REFERENCES category(category_id)
) ENGINE=InnoDB;

CREATE INDEX idx_category_parent ON category(category_parent_id);