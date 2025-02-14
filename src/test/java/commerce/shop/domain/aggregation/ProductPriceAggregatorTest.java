package commerce.shop.domain.aggregation;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.aggregation.model.CategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.PriceInformation;
import commerce.shop.domain.product.Category;
import commerce.shop.domain.product.ProductPriceSummary;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ProductPriceAggregator.class)
class ProductPriceAggregatorTest {

    @Autowired
    private ProductPriceAggregator aggregator;

    @Test
    @DisplayName("각 카테고리 별 최저가와 해당 브랜드를 찾는다")
    void aggregateMinimumPriceByCategoryTest() {
        // given
        long outerMinimPriceBrandId = 1L;
        int outerMinimumPrice = 5000;

        long topMinimumPriceBrandId = 2L;
        int topMinimumPrice = 8000;

        List<ProductPriceSummary> prices = List.of(
                new ProductPriceSummary(Category.TOP, 1L, 10000, 12000),
                new ProductPriceSummary(Category.TOP, topMinimumPriceBrandId, topMinimumPrice, 9000),
                new ProductPriceSummary(Category.OUTER, outerMinimPriceBrandId, outerMinimumPrice, 25000),
                new ProductPriceSummary(Category.OUTER, 2L, 6000, 24000)
        );

        // when
        CategoryPriceAggregation result = aggregator.aggregateMinimumPriceByCategory(prices);

        // then
        PriceInformation topPrice = result.priceFor(Category.TOP).orElseThrow();
        PriceInformation outerPrice = result.priceFor(Category.OUTER).orElseThrow();

        then(topPrice.branId()).isEqualTo(topMinimumPriceBrandId);
        then(topPrice.price()).isEqualTo(topMinimumPrice);
        then(outerPrice.branId()).isEqualTo(outerMinimPriceBrandId);
        then(outerPrice.price()).isEqualTo(outerMinimumPrice);
    }
}
