package commerce.shop.application.service.model;

import commerce.shop.domain.category.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryPrice {

    private final Category category;
    private final int price;
}
