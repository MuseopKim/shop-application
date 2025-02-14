package commerce.shop.domain.aggregation.model;

import commerce.shop.domain.product.Category;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryPriceAggregation {

    private final Map<Category, ProductPrice> categoryPrices;

    public CategoryPriceAggregation(Collection<ProductPrice> productPrices) {
        if (Objects.isNull(productPrices)) {
            this.categoryPrices = Collections.emptyMap();
            return;
        }

        this.categoryPrices = productPrices.stream()
                .collect(Collectors.toMap(ProductPrice::category, Function.identity()));
    }

    public int calculateTotalPrice() {
        return categoryPrices.values()
                .stream()
                .map(ProductPrice::price)
                .reduce(0, Integer::sum);
    }

    public Optional<ProductPrice> priceFor(Category category) {
        return Optional.ofNullable(categoryPrices.get(category));
    }
}
