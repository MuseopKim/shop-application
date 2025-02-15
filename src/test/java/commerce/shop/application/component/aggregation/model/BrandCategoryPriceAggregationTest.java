package commerce.shop.application.component.aggregation.model;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.category.Category;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BrandCategoryPriceAggregationTest {

    private final List<ProductPrice> prices = Arrays.asList(
            new ProductPrice(1L, Category.TOP, 10000),
            new ProductPrice(1L, Category.OUTER, 5000),
            new ProductPrice(1L, Category.PANTS, 3000),
            new ProductPrice(2L, Category.TOP, 11000),
            new ProductPrice(2L, Category.OUTER, 6000),
            new ProductPrice(2L, Category.PANTS, 4000)
    );

    @Test
    void calculateTotalPriceForValidBrandId() {
        // given
        BrandCategoryPriceAggregation aggregation = new BrandCategoryPriceAggregation(prices);

        // when
        int totalPrice = aggregation.calculateMinimumTotalPriceOf(1L);

        // then
        then(totalPrice).isEqualTo(18000); // 10000 + 5000 + 3000
    }

    @Test
    void returnZeroForNonExistentBrandId() {
        // given
        BrandCategoryPriceAggregation aggregation = new BrandCategoryPriceAggregation(prices);

        // when
        int totalPrice = aggregation.calculateMinimumTotalPriceOf(Long.MAX_VALUE);

        // then
        then(totalPrice).isZero();
    }

    @DisplayName("최저 총액 브랜드 ID를 찾는다")
    @Test
    void findMinimumTotalPriceBrandId() {
        // given
        BrandCategoryPriceAggregation aggregation = new BrandCategoryPriceAggregation(prices);
        // Brand 1 총액 : 18000, Brand 2 총액 : 21000
        long expectedBrandId = 1L;

        // when
        long actualBrandId = aggregation.minimumTotalPriceBrandId();

        // then
        then(actualBrandId).isEqualTo(expectedBrandId);
    }

    @DisplayName("특정 브랜드의 모든 카테고리 최저 가격을 조회한다")
    @Test
    void findMinimumPricesOfBrand() {
        // given
        BrandCategoryPriceAggregation aggregation = new BrandCategoryPriceAggregation(prices);
        long brandId = 1L;
        List<ProductPrice> givenPrices = Arrays.asList(
                new ProductPrice(1L, Category.TOP, 10000),
                new ProductPrice(1L, Category.OUTER, 5000),
                new ProductPrice(1L, Category.PANTS, 3000)
        );

        // when
        List<ProductPrice> prices = aggregation.minimumPricesOf(brandId);

        // then
        then(prices)
                .hasSameSizeAs(givenPrices)
                .usingRecursiveComparison()
                .isEqualTo(givenPrices);
    }
}
