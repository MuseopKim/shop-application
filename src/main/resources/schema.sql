CREATE TABLE brand
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '-',
    name       VARCHAR(100) NOT NULL COMMENT '브랜드명',
    created_at DATETIME     NOT NULL COMMENT '생성일',
    updated_at DATETIME     NOT NULL COMMENT '수정일'
);

CREATE TABLE product
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '-',
    brand_id   BIGINT       NOT NULL,
    category   VARCHAR(50)  NOT NULL,
    name       VARCHAR(100) NOT NULL,
    price      INTEGER      NOT NULL,
    created_at DATETIME     NOT NULL COMMENT '생성일',
    updated_at DATETIME     NOT NULL COMMENT '수정일'
);

CREATE INDEX `idx_product_category_brand_price` ON product (`category`, `brand_id`, `price`);
CREATE INDEX `idx_product_brand_id` ON product (`brand_id`);
