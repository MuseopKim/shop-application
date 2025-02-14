package commerce.shop.application.component.aggregation.model;

import commerce.shop.domain.category.Category;

public record ProductPrice(
        long brandId,
        Category category,
        int price
) {

}
