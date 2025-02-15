package commerce.shop.application.service.model;

import commerce.shop.domain.category.Category;

public record ProductMutationCommand(
        long brandId,
        Category category,
        String name,
        int price
) {

}
