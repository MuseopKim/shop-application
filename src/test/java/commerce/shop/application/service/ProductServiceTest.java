package commerce.shop.application.service;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import commerce.shop.api.controller.model.CategoryPrice;
import commerce.shop.api.controller.model.CategoryPrices;
import commerce.shop.application.component.aggregation.ProductPriceAggregator;
import commerce.shop.application.component.aggregation.model.CategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.ProductPrice;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.brand.Brands;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.PriceType;
import commerce.shop.domain.product.ProductPriceSummary;
import commerce.shop.domain.product.ProductReader;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductReader productReader;

    @Mock
    private BrandReader brandReader;

    @Mock
    private ProductPriceAggregator priceAggregator;

    @InjectMocks
    private ProductService productService;

    @DisplayName("카테고리 별 최저가 조회")
    @Test
    void retrieveCategoryMinimumPricesTest() {
        // given
        List<ProductPriceSummary> priceSummaries = List.of(
                new ProductPriceSummary(Category.TOP, 1L, 10000, 12000),
                new ProductPriceSummary(Category.OUTER, 2L, 5000, 7000)
        );

        List<ProductPrice> productPrices = List.of(
                new ProductPrice(1L, Category.TOP, 10000),
                new ProductPrice(2L, Category.OUTER, 5000)
        );

        Brands brands = Brands.of(List.of(
                brand().id(1L).name("브랜드A").build(),
                brand().id(2L).name("브랜드B").build()
        ));

        CategoryPriceAggregation aggregation = new CategoryPriceAggregation(productPrices, productPrices);

        // when
        when(productReader.readAllPriceSummaries()).thenReturn(priceSummaries);
        when(priceAggregator.aggregatePricesOfCategory(priceSummaries)).thenReturn(aggregation);
        when(brandReader.readAllByIds(Set.of(1L, 2L))).thenReturn(brands);

        CategoryPrices result = productService.retrieveCategoryMinimumPrices();

        // then
        then(result).isNotNull();
        then(result.getTotalPrice()).isEqualTo(15000);
        then(result.getPrices()).hasSize(2);

        CategoryPrice firstPrice = result.getPrices().get(0);
        then(firstPrice.getCategory()).isEqualTo(Category.TOP);
        then(firstPrice.getBrandName()).isEqualTo("브랜드A");
        then(firstPrice.getPrice()).isEqualTo(10000);
    }
}
