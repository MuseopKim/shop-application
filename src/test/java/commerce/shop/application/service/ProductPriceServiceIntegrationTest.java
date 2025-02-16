package commerce.shop.application.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import commerce.shop.api.controller.model.BrandMinimumTotalPricePayload;
import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.api.controller.model.CategoryPriceRangePayload;
import commerce.shop.cache.IntegrationTest;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.ProductReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class ProductPriceServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductPriceService productPriceService;

    @MockitoSpyBean
    private ProductReader spyProductReader;

    @DisplayName("카테고리 별 최저가 조회시 캐시가 적용되는지 확인한다")
    @Test
    void verifyCategoryMinimumPricesCaching() {
        CategoryMinimumPricesPayload firstResult =
                productPriceService.retrieveCategoryMinimumPrices();

        CategoryMinimumPricesPayload secondResult =
                productPriceService.retrieveCategoryMinimumPrices();

        verify(spyProductReader, times(1)).readAllPriceSummaries();
        then(firstResult).isEqualTo(secondResult);
    }

    @DisplayName("브랜드 별 전체 카테고리 최저가 조회시 캐시가 적용되는지 확인한다")
    @Test
    void verifyBrandMinimumTotalPriceCaching() {
        BrandMinimumTotalPricePayload firstResult =
                productPriceService.retrieveBrandMinimumTotalPrice();

        BrandMinimumTotalPricePayload secondResult =
                productPriceService.retrieveBrandMinimumTotalPrice();

        verify(spyProductReader, times(1)).readAllPriceSummaries();
        then(firstResult).isEqualTo(secondResult);
    }

    @DisplayName("카테고리 가격 범위 조회시 캐시가 적용되는지 확인한다")
    @Test
    void verifyCategoryPriceRangesCaching() {
        Category category = Category.TOP;

        CategoryPriceRangePayload firstResult =
                productPriceService.retrieveCategoryPriceRanges(category);

        CategoryPriceRangePayload secondResult =
                productPriceService.retrieveCategoryPriceRanges(category);

        verify(spyProductReader, times(1)).readPriceSummaries(category);
        then(firstResult).isEqualTo(secondResult);
    }
}
