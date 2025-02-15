package commerce.shop.api.controller.model;

import commerce.shop.application.service.model.PriceWithBrand;
import commerce.shop.domain.category.Category;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryPriceRangePayload {

    public static final CategoryPriceRangePayload EMPTY = CategoryPriceRangePayload.builder().build();

    private final Category category;
    private final List<PriceWithBrand> minimumPrices;
    private final List<PriceWithBrand> maximumPrices;
}
