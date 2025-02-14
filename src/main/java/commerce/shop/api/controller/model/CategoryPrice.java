package commerce.shop.api.controller.model;

import commerce.shop.domain.category.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryPrice {

    private final Category category;
    private final String brandName;
    private final int price;
}
