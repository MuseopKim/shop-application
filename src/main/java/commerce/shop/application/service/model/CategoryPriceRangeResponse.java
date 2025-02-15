package commerce.shop.application.service.model;

import commerce.shop.domain.category.Category;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryPriceRangeResponse {

    public static final CategoryPriceRangeResponse EMPTY = CategoryPriceRangeResponse.builder().build();

    private final Category category;
    private final List<BrandPrice> minimumPrices;
    private final List<BrandPrice> maximumPrices;
}
