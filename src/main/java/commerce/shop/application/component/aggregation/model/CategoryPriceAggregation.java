package commerce.shop.application.component.aggregation.model;

import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.PriceType;
import commerce.shop.global.exception.ApiExceptionCode;
import commerce.shop.global.exception.CategoryException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 카테고리별 최저/최고 가격을 집계하는 객체입니다.
 * 각 카테고리의 최저/최고 가격과 브랜드 정보를 보관하고,
 * 전체 카테고리의 가격을 집계하여 총액을 계산합니다.
 */
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
                        .orElseThrow(() -> new CategoryException(ApiExceptionCode.CATEGORY_NOT_EXIST));

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
