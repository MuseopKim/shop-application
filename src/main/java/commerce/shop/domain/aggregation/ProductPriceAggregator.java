package commerce.shop.domain.aggregation;

import commerce.shop.domain.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.CategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.ProductPrice;
import commerce.shop.domain.product.Category;
import commerce.shop.domain.product.ProductPriceSummary;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceAggregator {

    /**
     * 카테고리 별 최소 가격 정보
     */
    public CategoryPriceAggregation aggregateMinimumPricesOfCategory(Collection<ProductPriceSummary> productPrices) {
        List<ProductPrice> minimumPricesOfCategory = minimumPricesOfEachCategory(productPrices);

        return new CategoryPriceAggregation(minimumPricesOfCategory);
    }

    /**
     * 브랜드 별 모든 카테고리 최소 가격 정보
     */
    public BrandCategoryPriceAggregation aggregateBrandCategoryMinimumPrices(List<ProductPriceSummary> productPrices) {
        Map<Long, List<ProductPriceSummary>> productPricesByBrandId = productPrices.stream()
                .collect(Collectors.groupingBy(ProductPriceSummary::brandId));

        List<ProductPrice> allCategorySatisfiedBrandPrices = productPricesByBrandId.entrySet()
                .stream()
                .filter(entry -> this.containsAllCategories(entry.getValue()))
                .flatMap(entry -> this.minimumPricesOfEachCategory(entry.getValue()).stream())
                .toList();

        return new BrandCategoryPriceAggregation(allCategorySatisfiedBrandPrices);
    }

    private boolean containsAllCategories(Collection<ProductPriceSummary> productPrices) {
        Set<Category> categories = productPrices.stream()
                .map(ProductPriceSummary::category)
                .collect(Collectors.toUnmodifiableSet());

        return categories.containsAll(EnumSet.allOf(Category.class));
    }

    private List<ProductPrice> minimumPricesOfEachCategory(Collection<ProductPriceSummary> productPrices) {
        Map<Category, ProductPriceSummary> minimumPricesOfCategory = productPrices.stream()
                .collect(Collectors.toMap(ProductPriceSummary::category, Function.identity(),
                        (one, other) -> one.minimumPrice() <= other.maximumPrice() ? one : other));

        return minimumPricesOfCategory.values()
                .stream()
                .map(price -> new ProductPrice(price.brandId(), price.category(), price.minimumPrice()))
                .toList();
    }
}
