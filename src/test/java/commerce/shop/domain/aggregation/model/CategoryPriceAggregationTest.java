package commerce.shop.domain.aggregation.model;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.product.Category;
import commerce.shop.domain.product.PriceType;
import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryPriceAggregationTest {

    private final List<ProductPrice> minimumPrices = List.of(
            new ProductPrice(1L, Category.TOP, 10000),
            new ProductPrice(2L, Category.OUTER, 5000),
            new ProductPrice(3L, Category.PANTS, 3000)
    );

    private final List<ProductPrice> maximumPrices = List.of(
            new ProductPrice(1L, Category.TOP, 15000),
            new ProductPrice(2L, Category.OUTER, 7000),
            new ProductPrice(3L, Category.PANTS, 5000)
    );

    private final CategoryPriceAggregation aggregation = new CategoryPriceAggregation(minimumPrices, maximumPrices);

    @Test
    void calculateTotalPriceTest() {
        // given
        int expected = 18000; // 10000 + 5000 + 3000

        // when
        int totalPrice = aggregation.calculateTotalPrice(PriceType.MINIMUM_PRICE);

        // then
        then(totalPrice).isEqualTo(expected);
    }
}
