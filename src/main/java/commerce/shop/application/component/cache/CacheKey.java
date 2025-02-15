package commerce.shop.application.component.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheKey {

    public static final String CATEGORY_PRICE_MINIMUM = "category:price:minimum";
    public static final String BRAND_PRICE_MINIMUM_TOTAL = "brand:price:minimumTotal";
    public static final String CATEGORY_PRICE_RANGE = "category:price:range:%s";
}
