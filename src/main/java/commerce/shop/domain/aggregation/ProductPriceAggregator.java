package commerce.shop.domain.aggregation;

import commerce.shop.domain.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.CategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.ProductPrice;
import commerce.shop.domain.product.Category;
import commerce.shop.domain.product.PriceType;
import commerce.shop.domain.product.ProductPriceSummary;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceAggregator {

    /**
     * 카테고리 별 최대 / 최소 가격 정보
     */
    public CategoryPriceAggregation aggregatePricesOfCategory(Collection<ProductPriceSummary> productPrices) {
        List<ProductPrice> minimumPricesPerCategory = onePricePerCategory(productPrices, PriceType.MINIMUM_PRICE);
        List<ProductPrice> maximumPricePerCategory = onePricePerCategory(productPrices, PriceType.MAXIMUM_PRICE);

        return new CategoryPriceAggregation(minimumPricesPerCategory, maximumPricePerCategory);
    }

    /**
     * 브랜드 별 모든 카테고리 최소 가격 정보
     */
    public BrandCategoryPriceAggregation aggregateBrandCategoryMinimumPrices(Collection<ProductPriceSummary> productPrices) {
        Map<Long, List<ProductPriceSummary>> productPricesByBrandId = productPrices.stream()
                .collect(Collectors.groupingBy(ProductPriceSummary::brandId));

        List<ProductPrice> allCategorySatisfiedBrandPrices = productPricesByBrandId.entrySet()
                .stream()
                .filter(entry -> containsAllCategories(entry.getValue()))
                .flatMap(entry -> onePricePerCategory(entry.getValue(), PriceType.MINIMUM_PRICE).stream())
                .toList();

        return new BrandCategoryPriceAggregation(allCategorySatisfiedBrandPrices);
    }

    private boolean containsAllCategories(Collection<ProductPriceSummary> productPrices) {
        Set<Category> categories = productPrices.stream()
                .map(ProductPriceSummary::category)
                .collect(Collectors.toUnmodifiableSet());

        return categories.containsAll(EnumSet.allOf(Category.class));
    }

    private List<ProductPrice> onePricePerCategory(
            Collection<ProductPriceSummary> productPrices,
            PriceType priceType) {
        BinaryOperator<ProductPriceSummary> mergeFunction = (one, other) -> {
            if (priceType == PriceType.MAXIMUM_PRICE) {
                return one.maximumPrice() >= other.maximumPrice() ? one : other;
            }

            return one.minimumPrice() <= other.minimumPrice() ? one : other;
        };

        Map<Category, ProductPriceSummary> onePricePerCategory = productPrices.stream()
                .collect(Collectors.toMap(ProductPriceSummary::category,
                        Function.identity(),
                        mergeFunction));

        return onePricePerCategory.values()
                .stream()
                .map(price -> new ProductPrice(
                        price.brandId(),
                        price.category(),
                        priceType == PriceType.MINIMUM_PRICE
                                ? price.minimumPrice()
                                : price.maximumPrice()))
                .toList();
    }
}
