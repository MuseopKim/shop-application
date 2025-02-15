package commerce.shop.application.component.aggregation.model;

import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.PriceType;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryPriceAggregation {

    private final Map<Category, ProductPrice> minimumPrices;
    private final Map<Category, ProductPrice> maximumPrices;

    public CategoryPriceAggregation(Collection<ProductPrice> minimumPrices, Collection<ProductPrice> maximumPrices) {
        this.minimumPrices = new HashMap<>();
        this.maximumPrices = new HashMap<>();

        this.minimumPrices.putAll(minimumPrices.stream()
                .collect(Collectors.toMap(ProductPrice::category, Function.identity())));
        this.maximumPrices.putAll(maximumPrices.stream()
                .collect(Collectors.toMap(ProductPrice::category, Function.identity())));
    }

    public ProductPrice priceOf(Category category, PriceType priceType) {
        Function<Map<Category, ProductPrice>, ProductPrice> getPriceOfCategory =
                prices -> Optional.ofNullable(prices.get(category))
                        .orElseThrow(() -> new IllegalStateException("category " + category + " not exist."));

        if (priceType == PriceType.MINIMUM_PRICE) {
            return getPriceOfCategory.apply(minimumPrices);
        }

        return getPriceOfCategory.apply(maximumPrices);
    }

    public List<ProductPrice> allPricesOf(PriceType priceType) {
        if (priceType == PriceType.MINIMUM_PRICE) {
            return minimumPrices.values()
                    .stream()
                    .toList();
        }

        return maximumPrices.values()
                .stream()
                .toList();
    }

    public int calculateTotalPrice(PriceType priceType) {
        if (priceType == PriceType.MINIMUM_PRICE) {
            return minimumPrices.values()
                    .stream()
                    .map(ProductPrice::price)
                    .reduce(0, Integer::sum);
        }

        return maximumPrices.values()
                .stream()
                .map(ProductPrice::price)
                .reduce(0, Integer::sum);
    }
}
