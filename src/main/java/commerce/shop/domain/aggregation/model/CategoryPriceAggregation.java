package commerce.shop.domain.aggregation.model;

import commerce.shop.domain.product.Category;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CategoryPriceAggregation {

    private final Map<Category, PriceInformation> categoryPrices;

    public CategoryPriceAggregation(Map<Category, PriceInformation> categoryPrices) {
        this.categoryPrices = Optional.ofNullable(categoryPrices)
                .orElseGet(Collections::emptyMap);
    }

    public int calculateTotalPrice() {
        return categoryPrices.values()
                .stream()
                .map(PriceInformation::price)
                .reduce(0, Integer::sum);
    }

    public Optional<PriceInformation> priceFor(Category category) {
        return Optional.ofNullable(categoryPrices.get(category));
    }
}
