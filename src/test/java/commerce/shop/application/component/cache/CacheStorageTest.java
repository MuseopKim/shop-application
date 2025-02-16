package commerce.shop.application.component.cache;

import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.cache.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CacheStorageTest extends IntegrationTest {

    @Autowired
    private CacheStorage cacheStorage;

    @DisplayName("캐시가 존재하지 않을 경우 빈 Optional을 반환한다")
    @Test
    void getReturnsEmptyWhenCacheNotExistsTest() {
        // when
        Optional<String> result = cacheStorage.get("non_existent_key", String.class);

        // then
        then(result).isEmpty();
    }

    @DisplayName("캐시가 존재할 경우 캐시된 값을 반환한다")
    @Test
    void getReturnsCachedValueTest() {
        // given
        String key = "test_key";
        String value = "test_value";
        cacheStorage.set(key, value);

        // when
        Optional<String> result = cacheStorage.get(key, String.class);

        // then
        then(result).isPresent();
        then(result.get()).isEqualTo(value);
    }

    @DisplayName("값을 캐시에 저장한다")
    @Test
    void setStoresValueInCacheTest() {
        // given
        String key = CacheKey.CATEGORY_PRICE_MINIMUM;
        PriceResponse value = new PriceResponse("brandA", 10000);

        // when
        cacheStorage.set(key, value);

        // then
        Optional<PriceResponse> result = cacheStorage.get(key, PriceResponse.class);
        then(result).isPresent();
        then(result.get()).isEqualTo(value);
    }

    @DisplayName("캐시가 존재할 경우 getOrElse는 캐시된 값을 반환한다")
    @Test
    void getOrElseReturnsCachedValueTest() {
        // given
        String key = CacheKey.BRAND_PRICE_MINIMUM_TOTAL;
        PriceResponse cachedValue = new PriceResponse("brandA", 10000);
        cacheStorage.set(key, cachedValue);

        // when
        PriceResponse result = cacheStorage.getOrElse(
                key,
                PriceResponse.class,
                () -> new PriceResponse("brandB", 20000)
        );

        // then
        then(result).isEqualTo(cachedValue);
    }

    @DisplayName("캐시가 존재하지 않을 경우 getOrElse는 콜백을 실행하고 결과를 캐시한다")
    @Test
    void getOrElseExecutesCallbackAndCachesResultTest() {
        // given
        String key = CacheKey.BRAND_PRICE_MINIMUM_TOTAL;
        PriceResponse newValue = new PriceResponse("brandA", 10000);

        // when
        PriceResponse result = cacheStorage.getOrElse(
                key,
                PriceResponse.class,
                () -> newValue
        );

        // then
        then(result).isEqualTo(newValue);
        Optional<PriceResponse> cachedValue = cacheStorage.get(key, PriceResponse.class);
        then(cachedValue).isPresent();
        then(cachedValue.get()).isEqualTo(newValue);
    }

    @DisplayName("캐시에서 값을 제거한다")
    @Test
    void evictDeletesValueFromCacheTest() {
        // given
        String key = String.format(CacheKey.CATEGORY_PRICE_RANGE, "TOP");
        PriceResponse value = new PriceResponse("brandA", 10000);
        cacheStorage.set(key, value);

        // when
        cacheStorage.evict(key);

        // then
        Optional<PriceResponse> result = cacheStorage.get(key, PriceResponse.class);
        then(result).isEmpty();
    }
}

record PriceResponse(
        String brandName,
        int price
) {


}
