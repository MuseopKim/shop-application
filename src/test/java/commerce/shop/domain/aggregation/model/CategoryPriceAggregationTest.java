package commerce.shop.domain.aggregation.model;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.product.Category;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CategoryPriceAggregationTest {

    private Map<Category, PriceInformation> pricesByCategory = Map.of(
            Category.TOP, new PriceInformation(1L, 10000),
            Category.OUTER, new PriceInformation(2L, 5000),
            Category.PANTS, new PriceInformation(3L, 3000)
    );

    private CategoryPriceAggregation aggregation = new CategoryPriceAggregation(pricesByCategory);

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
