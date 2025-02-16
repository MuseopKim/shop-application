package commerce.shop.application.component.event;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import commerce.shop.api.controller.model.BrandMinimumTotalPricePayload;
import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.api.controller.model.CategoryPriceRangePayload;
import commerce.shop.api.controller.model.ProductPayload;
import commerce.shop.application.component.cache.CacheKey;
import commerce.shop.application.component.cache.CacheStorage;
import commerce.shop.application.service.ProductService;
import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.cache.IntegrationTest;
import commerce.shop.domain.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class ProductEventIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheStorage cacheStorage;

    @MockitoSpyBean
    private ProductEventListener eventListener;

    @DisplayName("상품 변경 시 트랜잭션 커밋 후에 캐시가 제거되어야 한다")
    @Test
    void whenProductModifiedthenEvictCacheAfterCommitTest() {
        // given
        ProductMutationCommand command =
                new ProductMutationCommand(1L, Category.TOP, "name", 10000);

        ProductPayload product = productService.registerProduct(command);

        ProductMutationCommand updateCommand =
                new ProductMutationCommand(1L, Category.TOP, "modified name", 20000);

        // when
        productService.modifyProduct(product.getId(), updateCommand);

        // then
        verify(eventListener, times(1))
                .handle(argThat(event -> event.getType() == ProductEventType.PRODUCT_UPDATED));

        then(cacheStorage.get(CacheKey.CATEGORY_PRICE_MINIMUM, CategoryMinimumPricesPayload.class)).isEmpty();
        then(cacheStorage.get(CacheKey.BRAND_PRICE_MINIMUM_TOTAL, BrandMinimumTotalPricePayload.class)).isEmpty();
        then(cacheStorage.get(String.format(CacheKey.CATEGORY_PRICE_RANGE, Category.TOP.name()), CategoryPriceRangePayload.class))
                .isEmpty();
    }

    @DisplayName("상품 삭제 실패 시 캐시 제거가 실행되지 않아야 한다")
    @Test
    void whenProductDeleteFailsThenShouldNotEvictCacheTest() {
        // given
        long nonExistentProductId = 999L;

        thenThrownBy(() -> productService.removeProduct(nonExistentProductId))
                .isExactlyInstanceOf(RuntimeException.class);

        verify(eventListener, never()).handle(any());
    }
}
