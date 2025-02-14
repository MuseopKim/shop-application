package commerce.shop.domain.aggregation.model;

import commerce.shop.domain.product.Category;

public record ProductPrice(
        long branId,
        Category category,
        int price
) {

}
