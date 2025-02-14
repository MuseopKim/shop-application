package commerce.shop.domain.aggregation.model;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.product.Category;
import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryPriceAggregationTest {

    private final List<ProductPrice> prices = List.of(
            new ProductPrice(1L, Category.TOP, 10000),
            new ProductPrice(2L, Category.OUTER, 5000),
            new ProductPrice(3L, Category.PANTS, 3000)
    );

    private final CategoryPriceAggregation aggregation = new CategoryPriceAggregation(prices);

    @Test
    void calculateTotalPriceTest() {
        // given
        int expected = 18000; // 10000 + 5000 + 3000

        // when
        int totalPrice = aggregation.calculateTotalPrice();

        // then
        then(totalPrice).isEqualTo(expected);
    }
}
