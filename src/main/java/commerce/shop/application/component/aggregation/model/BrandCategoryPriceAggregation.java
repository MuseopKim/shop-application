package commerce.shop.application.component.aggregation.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  브랜드별로 전체 카테고리 상품의 가격을 집계하는 객체입니다.
 *  브랜드가 보유한 전체 카테고리 상품의 최저 가격을 집계하고,
 *  브랜드별 총액을 계산하여 최저가 브랜드를 선정합니다.
 */
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
