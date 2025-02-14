package commerce.shop.domain.aggregation.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BrandCategoryPriceAggregation {

    private final Map<Long, List<ProductPrice>> brandCategoryPrices;

    public BrandCategoryPriceAggregation(List<ProductPrice> prices) {
        if (Objects.isNull(prices)) {
            this.brandCategoryPrices = Collections.emptyMap();
            return;
        }

        this.brandCategoryPrices = prices.stream()
                .collect(Collectors.groupingBy(ProductPrice::branId));
    }

    public int minimumTotalPriceOf(long brandId) {
        List<ProductPrice> brandCategoryPrices = this.brandCategoryPrices.getOrDefault(brandId, Collections.emptyList());

        return brandCategoryPrices
                .stream()
                .map(ProductPrice::price)
                .reduce(0, Integer::sum);
    }
}
