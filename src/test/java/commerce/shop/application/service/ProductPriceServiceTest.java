package commerce.shop.application.service;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import commerce.shop.api.controller.model.BrandMinimumTotalPricePayload;
import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.api.controller.model.CategoryPriceRangePayload;
import commerce.shop.application.component.aggregation.ProductPriceAggregator;
import commerce.shop.application.component.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.CategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.ProductPrice;
import commerce.shop.application.service.model.PriceWithBrand;
import commerce.shop.application.service.model.PriceWithCategory;
import commerce.shop.application.service.model.PriceWithCategoryAndBrand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.brand.Brands;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.PriceType;
import commerce.shop.domain.product.ProductPriceSummary;
import commerce.shop.domain.product.ProductReader;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductPriceServiceTest {

    @Mock
    private ProductReader productReader;

    @Mock
    private BrandReader brandReader;

    @Mock
    private ProductPriceAggregator priceAggregator;

    @InjectMocks
    private ProductPriceService productPriceService;

    @DisplayName("카테고리 별 최저가를 조회한다")
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

        CategoryMinimumPricesPayload result = productPriceService.fetchCategoryMinimumPrices();

        // then
        then(result).isNotNull();
        then(result.getTotalPrice()).isEqualTo(15000);
        then(result.getPrices()).hasSize(2);

        PriceWithCategoryAndBrand firstPrice = result.getPrices().get(0);
        then(firstPrice.getCategory()).isEqualTo(Category.TOP);
        then(firstPrice.getBrandName()).isEqualTo("브랜드A");
        then(firstPrice.getPrice()).isEqualTo(10000);
    }

    @DisplayName("브랜드별 전체 카테고리 최저가를 조회한다")
    @Test
    void retrieveBrandMinimumTotalPriceTest() {
        // given
        List<ProductPriceSummary> summaries = List.of(
                new ProductPriceSummary(Category.TOP, 1L, 10000, 12000),
                new ProductPriceSummary(Category.OUTER, 1L, 5000, 7000),
                new ProductPriceSummary(Category.PANTS, 1L, 3000, 4000),
                new ProductPriceSummary(Category.SNEAKERS, 1L, 9000, 10000),
                new ProductPriceSummary(Category.BAG, 1L, 2000, 3000),
                new ProductPriceSummary(Category.CAP, 1L, 1500, 2000),
                new ProductPriceSummary(Category.SOCKS, 1L, 2000, 2500),
                new ProductPriceSummary(Category.ACCESSORIES, 1L, 2000, 2500)
        );

        List<ProductPrice> prices = List.of(
                new ProductPrice(1L, Category.TOP, 10000),
                new ProductPrice(1L, Category.OUTER, 5000),
                new ProductPrice(1L, Category.PANTS, 3000),
                new ProductPrice(1L, Category.SNEAKERS, 9000),
                new ProductPrice(1L, Category.BAG, 2000),
                new ProductPrice(1L, Category.CAP, 1500),
                new ProductPrice(1L, Category.SOCKS, 2000),
                new ProductPrice(1L, Category.ACCESSORIES, 2000)
        );

        Brand brand = brand()
                .id(1L)
                .name("브랜드D")
                .build();

        BrandCategoryPriceAggregation aggregation = mock(BrandCategoryPriceAggregation.class);

        // when
        when(productReader.readAllPriceSummaries()).thenReturn(summaries);
        when(priceAggregator.aggregateBrandCategoryMinimumPrices(summaries)).thenReturn(aggregation);
        when(aggregation.minimumTotalPriceBrandId()).thenReturn(1L);
        when(brandReader.readById(1L)).thenReturn(Optional.of(brand));
        when(aggregation.minimumPricesOf(1L)).thenReturn(prices);
        when(aggregation.calculateMinimumTotalPriceOf(1L)).thenReturn(34500);

        BrandMinimumTotalPricePayload response = productPriceService.fetchBrandMinimumTotalPrice();

        // then
        then(response).isNotNull();
        then(response.getMinimumPrice().getBrandName()).isEqualTo("브랜드D");
        then(response.getMinimumPrice().getTotalPrice()).isEqualTo(34500);
        then(response.getMinimumPrice().getCategories()).hasSize(8);

        List<PriceWithCategory> categories = response.getMinimumPrice().getCategories();
        then(categories.get(0).getCategory()).isEqualTo(Category.TOP);
        then(categories.get(0).getPrice()).isEqualTo(10000);
        then(categories.get(1).getCategory()).isEqualTo(Category.OUTER);
        then(categories.get(1).getPrice()).isEqualTo(5000);
    }

    @DisplayName("카테고리의 최저가 / 최고가 브랜드와 가격을 조회한다")
    @Test
    void retrieveCategoryPriceRangesTest() {
        // given
        Category category = Category.TOP;

        List<ProductPriceSummary> summaries = List.of(
                new ProductPriceSummary(Category.TOP, 1L, 10000, 12000),
                new ProductPriceSummary(Category.TOP, 2L, 11400, 13000)
        );

        ProductPrice minimumPrice = new ProductPrice(1L, Category.TOP, 10000);
        ProductPrice maximumPrice = new ProductPrice(2L, Category.TOP, 11400);

        CategoryPriceAggregation aggregation = mock(CategoryPriceAggregation.class);

        Brands brands = Brands.of(List.of(
                brand().id(1L).name("브랜드C").build(),
                brand().id(2L).name("브랜드I").build()
        ));

        // when
        when(productReader.readPriceSummaries(category)).thenReturn(summaries);
        when(priceAggregator.aggregatePricesOfCategory(summaries)).thenReturn(aggregation);
        when(aggregation.priceOf(category, PriceType.MINIMUM_PRICE)).thenReturn(minimumPrice);
        when(aggregation.priceOf(category, PriceType.MAXIMUM_PRICE)).thenReturn(maximumPrice);
        when(brandReader.readAllByIds(Set.of(1L, 2L))).thenReturn(brands);

        CategoryPriceRangePayload response = productPriceService.fetchCategoryPriceRanges(category);

        // then
        then(response).isNotNull();

        List<PriceWithBrand> minimumPrices = response.getMinimumPrices();
        then(minimumPrices).hasSize(1);
        then(minimumPrices.get(0).getBrandName()).isEqualTo("브랜드C");
        then(minimumPrices.get(0).getPrice()).isEqualTo(10000);

        List<PriceWithBrand> maximumPrices = response.getMaximumPrices();
        then(maximumPrices).hasSize(1);
        then(maximumPrices.get(0).getBrandName()).isEqualTo("브랜드I");
        then(maximumPrices.get(0).getPrice()).isEqualTo(11400);
    }
}
