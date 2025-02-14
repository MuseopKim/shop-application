package commerce.shop.domain.aggregation;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.CategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.ProductPrice;
import commerce.shop.domain.product.Category;
import commerce.shop.domain.product.ProductPriceSummary;
import java.util.Collections;
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
    void aggregateMinimumPricesOfCategoryTest() {
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
        CategoryPriceAggregation result = aggregator.aggregateMinimumPricesOfCategory(prices);

        // then
        ProductPrice topPrice = result.priceFor(Category.TOP).orElseThrow();
        ProductPrice outerPrice = result.priceFor(Category.OUTER).orElseThrow();

        then(topPrice.branId()).isEqualTo(topMinimumPriceBrandId);
        then(topPrice.price()).isEqualTo(topMinimumPrice);
        then(outerPrice.branId()).isEqualTo(outerMinimPriceBrandId);
        then(outerPrice.price()).isEqualTo(outerMinimumPrice);
    }

    @Test
    @DisplayName("모든 카테고리를 가진 브랜드의 최소 가격을 집계한다")
    void aggregateMinimumPricesForBrandWithAllCategoriesTest() {
        // given
        long brandId = 1L;
        List<ProductPriceSummary> productPrices = List.of(
                new ProductPriceSummary(Category.TOP, brandId, 10000, 12000),
                new ProductPriceSummary(Category.OUTER, brandId, 5000, 7000),
                new ProductPriceSummary(Category.PANTS, brandId, 3000, 4000),
                new ProductPriceSummary(Category.SNEAKERS, brandId, 9000, 11000),
                new ProductPriceSummary(Category.BAG, brandId, 2000, 3000),
                new ProductPriceSummary(Category.CAP, brandId, 1500, 2500),
                new ProductPriceSummary(Category.SOCKS, brandId, 1000, 1500),
                new ProductPriceSummary(Category.ACCESSORIES, brandId, 2000, 3000)
        );

        // when
        BrandCategoryPriceAggregation result = aggregator.aggregateBrandCategoryMinimumPrices(productPrices);

        // then
        then(result.minimumTotalPriceOf(brandId)).isEqualTo(33500);
    }

    @Test
    @DisplayName("일부 카테고리가 없는 브랜드는 집계에서 제외된다")
    void excludeBrandsMissingCategories() {
        // given
        long brandId = 1L;
        List<ProductPriceSummary> productPrices = List.of(
                new ProductPriceSummary(Category.TOP, brandId, 10000, 12000),
                new ProductPriceSummary(Category.OUTER, brandId, 5000, 7000),
                new ProductPriceSummary(Category.SNEAKERS, brandId, 9000, 11000)
        );

        // when
        BrandCategoryPriceAggregation result = aggregator.aggregateBrandCategoryMinimumPrices(productPrices);

        // then
        then(result.minimumTotalPriceOf(brandId)).isZero();
    }

    @Test
    @DisplayName("여러 브랜드의 최소 가격을 올바르게 집계한다")
    void aggregateMinimumPricesForMultipleBrands() {
        // given
        long brand1Id = 1L;
        long brand2Id = 2L;
        List<ProductPriceSummary> productPrices = createMultipleBrandPrices(brand1Id, brand2Id);

        // when
        BrandCategoryPriceAggregation result = aggregator.aggregateBrandCategoryMinimumPrices(productPrices);

        // then
        then(result.minimumTotalPriceOf(brand1Id)).isEqualTo(33500);
        then(result.minimumTotalPriceOf(brand2Id)).isEqualTo(35000);
    }

    @Test
    @DisplayName("동일 카테고리 내 최소 가격이 선택된다")
    void selectMinimumPriceWithinCategory() {
        // given
        long brandId = 1L;
        List<ProductPriceSummary> productPrices = List.of(
                new ProductPriceSummary(Category.TOP, brandId, 10000, 12000),
                new ProductPriceSummary(Category.TOP, brandId, 12000, 15000),
                new ProductPriceSummary(Category.OUTER, brandId, 5000, 7000),
                new ProductPriceSummary(Category.OUTER, brandId, 6000, 8000),
                new ProductPriceSummary(Category.PANTS, brandId, 3000, 4000),
                new ProductPriceSummary(Category.SNEAKERS, brandId, 9000, 11000),
                new ProductPriceSummary(Category.BAG, brandId, 2000, 3000),
                new ProductPriceSummary(Category.CAP, brandId, 1500, 2500),
                new ProductPriceSummary(Category.SOCKS, brandId, 1000, 1500),
                new ProductPriceSummary(Category.ACCESSORIES, brandId, 2000, 3000)
        );

        // when
        BrandCategoryPriceAggregation result = aggregator.aggregateBrandCategoryMinimumPrices(productPrices);

        // then
        then(result.minimumTotalPriceOf(brandId)).isEqualTo(33500);
    }

    @Test
    @DisplayName("빈 리스트로 초기화된 경우 빈 결과를 반환한다")
    void returnEmptyResultForEmptyList() {
        // given
        List<ProductPriceSummary> emptyPrices = Collections.emptyList();

        // when
        BrandCategoryPriceAggregation result = aggregator.aggregateBrandCategoryMinimumPrices(emptyPrices);

        // then
        then(result.minimumTotalPriceOf(1L)).isZero();
    }

    private List<ProductPriceSummary> createMultipleBrandPrices(long brand1Id, long brand2Id) {
        return List.of(
                // Brand 1
                new ProductPriceSummary(Category.TOP, brand1Id, 10000, 12000),
                new ProductPriceSummary(Category.OUTER, brand1Id, 5000, 7000),
                new ProductPriceSummary(Category.PANTS, brand1Id, 3000, 4000),
                new ProductPriceSummary(Category.SNEAKERS, brand1Id, 9000, 11000),
                new ProductPriceSummary(Category.BAG, brand1Id, 2000, 3000),
                new ProductPriceSummary(Category.CAP, brand1Id, 1500, 2500),
                new ProductPriceSummary(Category.SOCKS, brand1Id, 1000, 1500),
                new ProductPriceSummary(Category.ACCESSORIES, brand1Id, 2000, 3000),

                // Brand 2
                new ProductPriceSummary(Category.TOP, brand2Id, 11000, 13000),
                new ProductPriceSummary(Category.OUTER, brand2Id, 5500, 7500),
                new ProductPriceSummary(Category.PANTS, brand2Id, 3500, 4500),
                new ProductPriceSummary(Category.SNEAKERS, brand2Id, 9500, 11500),
                new ProductPriceSummary(Category.BAG, brand2Id, 2100, 3100),
                new ProductPriceSummary(Category.CAP, brand2Id, 1400, 2400),
                new ProductPriceSummary(Category.SOCKS, brand2Id, 1000, 1500),
                new ProductPriceSummary(Category.ACCESSORIES, brand2Id, 1000, 2000)
        );
    }
}
