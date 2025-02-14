package commerce.shop.domain.aggregation.model;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.product.Category;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BrandCategoryPriceAggregationTest {

    private final List<BrandCategoryPrice> prices = Arrays.asList(
            new BrandCategoryPrice(1L, Category.TOP, 10000),
            new BrandCategoryPrice(1L, Category.OUTER, 5000),
            new BrandCategoryPrice(1L, Category.PANTS, 3000),
            new BrandCategoryPrice(2L, Category.TOP, 11000),
            new BrandCategoryPrice(2L, Category.OUTER, 6000),
            new BrandCategoryPrice(2L, Category.PANTS, 4000)
    );

    @Test
    void calculateTotalPriceForValidBrandId() {
        // given
        BrandCategoryPriceAggregation aggregation = new BrandCategoryPriceAggregation(prices);

        // when
        int totalPrice = aggregation.totalPriceOf(1L);

        // then
        then(totalPrice).isEqualTo(18000); // 10000 + 5000 + 3000
    }

    @Test
    void returnZeroForNonExistentBrandId() {
        // given
        BrandCategoryPriceAggregation aggregation = new BrandCategoryPriceAggregation(prices);

        // when
        int totalPrice = aggregation.totalPriceOf(Long.MAX_VALUE);

        // then
        then(totalPrice).isZero();
    }
}
