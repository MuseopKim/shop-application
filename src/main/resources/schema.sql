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
    name       VARCHAR(200) NOT NULL,
    price      INTEGER      NOT NULL,
    created_at DATETIME     NOT NULL COMMENT '생성일',
    updated_at DATETIME     NOT NULL COMMENT '수정일'
);

CREATE INDEX `idx_category_price_brand` ON product (`category`, `price`, `brand_id`);

-- 초기 데이터
INSERT INTO brand (name, created_at, updated_at)
VALUES ('A', NOW(), NOW()),
       ('B', NOW(), NOW()),
       ('C', NOW(), NOW()),
       ('D', NOW(), NOW()),
       ('E', NOW(), NOW()),
       ('F', NOW(), NOW()),
       ('G', NOW(), NOW()),
       ('H', NOW(), NOW()),
       ('I', NOW(), NOW());


INSERT INTO product (brand_id, category, name, price, created_at, updated_at)
VALUES (1, 'TOP', 'A 상의', 11200, NOW(), NOW()),
       (1, 'OUTER', 'A 아우터', 5500, NOW(), NOW()),
       (1, 'PANTS', 'A 바지', 4200, NOW(), NOW()),
       (1, 'SNEAKERS', 'A 스니커즈', 9000, NOW(), NOW()),
       (1, 'BAG', 'A 가방', 2000, NOW(), NOW()),
       (1, 'CAP', 'A 모자', 1700, NOW(), NOW()),
       (1, 'SOCKS', 'A 양말', 1800, NOW(), NOW()),
       (1, 'ACCESSORIES', 'A 액세서리', 2300, NOW(), NOW()),

       (2, 'TOP', 'B 상의', 10500, NOW(), NOW()),
       (2, 'OUTER', 'B 아우터', 5900, NOW(), NOW()),
       (2, 'PANTS', 'B 바지', 3800, NOW(), NOW()),
       (2, 'SNEAKERS', 'B 스니커즈', 9100, NOW(), NOW()),
       (2, 'BAG', 'B 가방', 2100, NOW(), NOW()),
       (2, 'CAP', 'B 모자', 2000, NOW(), NOW()),
       (2, 'SOCKS', 'B 양말', 2000, NOW(), NOW()),
       (2, 'ACCESSORIES', 'B 액세서리', 2200, NOW(), NOW()),

       (3, 'TOP', 'C 상의', 10000, NOW(), NOW()),
       (3, 'OUTER', 'C 아우터', 6200, NOW(), NOW()),
       (3, 'PANTS', 'C 바지', 3300, NOW(), NOW()),
       (3, 'SNEAKERS', 'C 스니커즈', 9200, NOW(), NOW()),
       (3, 'BAG', 'C 가방', 2200, NOW(), NOW()),
       (3, 'CAP', 'C 모자', 1900, NOW(), NOW()),
       (3, 'SOCKS', 'C 양말', 2200, NOW(), NOW()),
       (3, 'ACCESSORIES', 'C 액세서리', 2100, NOW(), NOW()),

       (4, 'TOP', 'D 상의', 10100, NOW(), NOW()),
       (4, 'OUTER', 'D 아우터', 5100, NOW(), NOW()),
       (4, 'PANTS', 'D 바지', 3000, NOW(), NOW()),
       (4, 'SNEAKERS', 'D 스니커즈', 9500, NOW(), NOW()),
       (4, 'BAG', 'D 가방', 2500, NOW(), NOW()),
       (4, 'CAP', 'D 모자', 1500, NOW(), NOW()),
       (4, 'SOCKS', 'D 양말', 2400, NOW(), NOW()),
       (4, 'ACCESSORIES', 'D 액세서리', 2000, NOW(), NOW()),

       (5, 'TOP', 'E 상의', 10700, NOW(), NOW()),
       (5, 'OUTER', 'E 아우터', 5000, NOW(), NOW()),
       (5, 'PANTS', 'E 바지', 3800, NOW(), NOW()),
       (5, 'SNEAKERS', 'E 스니커즈', 9900, NOW(), NOW()),
       (5, 'BAG', 'E 가방', 2300, NOW(), NOW()),
       (5, 'CAP', 'E 모자', 1800, NOW(), NOW()),
       (5, 'SOCKS', 'E 양말', 2100, NOW(), NOW()),
       (5, 'ACCESSORIES', 'E 액세서리', 2100, NOW(), NOW()),

       (6, 'TOP', 'F 상의', 11200, NOW(), NOW()),
       (6, 'OUTER', 'F 아우터', 7200, NOW(), NOW()),
       (6, 'PANTS', 'F 바지', 4000, NOW(), NOW()),
       (6, 'SNEAKERS', 'F 스니커즈', 9300, NOW(), NOW()),
       (6, 'BAG', 'F 가방', 2100, NOW(), NOW()),
       (6, 'CAP', 'F 모자', 1600, NOW(), NOW()),
       (6, 'SOCKS', 'F 양말', 2300, NOW(), NOW()),
       (6, 'ACCESSORIES', 'F 액세서리', 1900, NOW(), NOW()),

       (7, 'TOP', 'G 상의', 10500, NOW(), NOW()),
       (7, 'OUTER', 'G 아우터', 5800, NOW(), NOW()),
       (7, 'PANTS', 'G 바지', 3900, NOW(), NOW()),
       (7, 'SNEAKERS', 'G 스니커즈', 9000, NOW(), NOW()),
       (7, 'BAG', 'G 가방', 2200, NOW(), NOW()),
       (7, 'CAP', 'G 모자', 1700, NOW(), NOW()),
       (7, 'SOCKS', 'G 양말', 2100, NOW(), NOW()),
       (7, 'ACCESSORIES', 'G 액세서리', 2000, NOW(), NOW()),

       (8, 'TOP', 'H 상의', 10800, NOW(), NOW()),
       (8, 'OUTER', 'H 아우터', 6300, NOW(), NOW()),
       (8, 'PANTS', 'H 바지', 3100, NOW(), NOW()),
       (8, 'SNEAKERS', 'H 스니커즈', 9700, NOW(), NOW()),
       (8, 'BAG', 'H 가방', 2100, NOW(), NOW()),
       (8, 'CAP', 'H 모자', 1600, NOW(), NOW()),
       (8, 'SOCKS', 'H 양말', 2000, NOW(), NOW()),
       (8, 'ACCESSORIES', 'H 액세서리', 2000, NOW(), NOW()),

       (9, 'TOP', 'I 상의', 11400, NOW(), NOW()),
       (9, 'OUTER', 'I 아우터', 6700, NOW(), NOW()),
       (9, 'PANTS', 'I 바지', 3200, NOW(), NOW()),
       (9, 'SNEAKERS', 'I 스니커즈', 9500, NOW(), NOW()),
       (9, 'BAG', 'I 가방', 2400, NOW(), NOW()),
       (9, 'CAP', 'I 모자', 1700, NOW(), NOW()),
       (9, 'SOCKS', 'I 양말', 1700, NOW(), NOW()),
       (9, 'ACCESSORIES', 'I 액세서리', 2400, NOW(), NOW());
