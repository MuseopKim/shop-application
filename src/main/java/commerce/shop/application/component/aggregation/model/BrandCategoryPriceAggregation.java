package commerce.shop.application.component.aggregation.model;

import java.util.*;
import java.util.stream.Collectors;

public class BrandCategoryPriceAggregation {

    private final Map<Long, List<ProductPrice>> brandCategoryPrices;

    public BrandCategoryPriceAggregation(List<ProductPrice> prices) {
        if (Objects.isNull(prices)) {
            this.brandCategoryPrices = Collections.emptyMap();
            return;
        }

        this.brandCategoryPrices = prices.stream()
                .collect(Collectors.groupingBy(ProductPrice::brandId));
    }

    public int calculateMinimumTotalPriceOf(long brandId) {
        List<ProductPrice> brandCategoryPrices = this.brandCategoryPrices.getOrDefault(brandId, Collections.emptyList());

        return brandCategoryPrices
                .stream()
                .map(ProductPrice::price)
                .reduce(0, Integer::sum);
    }

    public List<ProductPrice> minimumPricesOf(long brandId) {
        return brandCategoryPrices.getOrDefault(brandId, Collections.emptyList());
    }

    public long minimumTotalPriceBrandId() {
        return brandCategoryPrices.keySet()
                .stream()
                .sorted(Comparator.comparingInt(this::calculateMinimumTotalPriceOf))
                .limit(1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No price founds"));
    }
}
