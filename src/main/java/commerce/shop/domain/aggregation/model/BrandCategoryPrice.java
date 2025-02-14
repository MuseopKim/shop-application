package commerce.shop.domain.aggregation.model;

import commerce.shop.domain.product.Category;

public record BrandCategoryPrice(
        long brandId,
        Category category,
        int price
) {

}
