package commerce.shop.global.constant;

public final class ValidationMessage {

    public static final class Brand {

        public static final String NAME_NOT_BLANK = "브랜드명은 필수입니다";
        public static final String NAME_SIZE = "브랜드명은 100자를 초과할 수 없습니다";
        public static final String NAME_INVALID = "브랜드명이 유효하지 않습니다";
    }

    public static final class Product {

        public static final String BRAND_ID_NOT_NULL = "브랜드 ID는 필수입니다";
        public static final String CATEGORY_NOT_BLANK = "카테고리는 필수입니다";
        public static final String NAME_NOT_BLANK = "상품명은 필수입니다";
        public static final String NAME_SIZE = "상품명은 100자를 초과할 수 없습니다";
        public static final String PRICE_NOT_NULL = "가격은 필수입니다";
        public static final String PRICE_MIN = "가격은 0원 이상이어야 합니다";
    }
}
