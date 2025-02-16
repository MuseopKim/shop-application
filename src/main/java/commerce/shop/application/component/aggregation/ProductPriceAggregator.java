package commerce.shop.application.component.aggregation;

import commerce.shop.application.component.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.CategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.ProductPrice;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.PriceType;
import commerce.shop.domain.product.ProductPriceSummary;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 상품 가격 데이터를 다양한 관점에서 집계하는 컴포넌트입니다.
 *
 * 조회된 가격 정보({@link ProductPriceSummary})를 기반으로 다음과 같은 집계를 수행합니다.
 * 카테고리별 최저/최고 가격 집계 ({@link CategoryPriceAggregation})
 * 브랜드별 전체 카테고리 가격 집계 ({@link BrandCategoryPriceAggregation})
 */
@Component
public class ProductPriceAggregator {

    /**
     * 카테고리 별 최대 / 최소 가격 정보 집계
     */
    public CategoryPriceAggregation aggregatePricesOfCategory(Collection<ProductPriceSummary> productPrices) {
        List<ProductPrice> minimumPricesPerCategory = onePricePerCategory(productPrices, PriceType.MINIMUM_PRICE);
        List<ProductPrice> maximumPricePerCategory = onePricePerCategory(productPrices, PriceType.MAXIMUM_PRICE);

        return new CategoryPriceAggregation(minimumPricesPerCategory, maximumPricePerCategory);
    }

    /**
     * 브랜드 별 모든 카테고리 최소 가격 정보 집계
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
