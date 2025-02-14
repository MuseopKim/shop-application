package commerce.shop.domain.aggregation.model;

import java.util.*;
import java.util.stream.Collectors;

public class BrandCategoryPriceAggregation {

    private final Map<Long, List<BrandCategoryPrice>> brandCategoryPrices;

    public BrandCategoryPriceAggregation(List<BrandCategoryPrice> prices) {
        if (Objects.isNull(prices)) {
            this.brandCategoryPrices = Collections.emptyMap();
            return;
        }

        this.brandCategoryPrices = prices.stream()
                .collect(Collectors.groupingBy(BrandCategoryPrice::brandId));
    }

    public int totalPriceOf(long brandId) {
        List<BrandCategoryPrice> brandCategoryPrices = this.brandCategoryPrices.getOrDefault(brandId, Collections.emptyList());

        return brandCategoryPrices.stream()
                .map(BrandCategoryPrice::price)
                .reduce(0, Integer::sum);
    }
}
