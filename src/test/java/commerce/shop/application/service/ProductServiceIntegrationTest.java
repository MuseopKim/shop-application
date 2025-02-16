package commerce.shop.application.service;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.api.controller.model.BrandMinimumTotalPricePayload;
import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.api.controller.model.CategoryPriceRangePayload;
import commerce.shop.application.component.cache.CacheKey;
import commerce.shop.application.component.cache.CacheStorage;
import commerce.shop.application.component.event.ProductEventPublisher;
import commerce.shop.application.service.model.*;
import commerce.shop.cache.IntegrationTest;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandWriter;
import commerce.shop.domain.category.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class ProductServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandWriter brandWriter;

    @MockitoSpyBean
    private ProductEventPublisher eventPublisher;

    @Autowired
    private CacheStorage cacheStorage;

    private final Category testCategory = Category.TOP;

    @DisplayName("상품이 추가되면 기존 캐시를 제거한다.")
    @Test
    void cacheShouldBeEvictedAfterCreateProductTest() {
        // given
        Brand brand = brandWriter.create(new BrandMutationCommand("brandName"));

        String brandName = brand.getName();
        int price = 10000;

        cacheStorage.set(CacheKey.CATEGORY_PRICE_MINIMUM, CategoryMinimumPricesPayload.builder()
                .prices(List.of(PriceWithCategoryAndBrand.builder()
                        .category(testCategory)
                        .brandName(brandName)
                        .price(price)
                        .build())));

        cacheStorage.set(CacheKey.BRAND_PRICE_MINIMUM_TOTAL, BrandMinimumTotalPricePayload.builder()
                .minimumPrice(BrandTotalPrice.builder()
                        .brandName(brandName)
                        .totalPrice(price)
                        .categories(List.of(PriceWithCategory.builder()
                                .category(testCategory)
                                .price(price)
                                .build()))
                        .build()));

        cacheStorage.set(String.format(CacheKey.CATEGORY_PRICE_RANGE, Category.TOP.name()),
                CategoryPriceRangePayload.builder()
                        .category(testCategory)
                        .minimumPrices(List.of(PriceWithBrand.builder()
                                .price(price)
                                .brandName(brandName)
                                .build()))
                        .maximumPrices(List.of(PriceWithBrand.builder()
                                .price(price)
                                .brandName(brandName)
                                .build())));

        // when
        ProductMutationCommand command =
                new ProductMutationCommand(brand.getId(),
                        testCategory,
                        "productName",
                        price);

        productService.registerProduct(command);

        // then
        then(cacheStorage.get(CacheKey.CATEGORY_PRICE_MINIMUM, CategoryMinimumPricesPayload.class)).isEmpty();
        then(cacheStorage.get(CacheKey.BRAND_PRICE_MINIMUM_TOTAL, BrandMinimumTotalPricePayload.class)).isEmpty();
        then(cacheStorage.get(String.format(CacheKey.CATEGORY_PRICE_RANGE, Category.TOP.name()), CategoryPriceRangePayload.class))
                .isEmpty();
    }
}
