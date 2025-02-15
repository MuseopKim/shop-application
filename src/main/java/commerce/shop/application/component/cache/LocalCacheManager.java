package commerce.shop.application.component.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import commerce.shop.global.config.cache.CacheProps;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 별도의 인프라 구성을 하지 않기 위해 글로벌 캐시가 아닌 로컬 캐시를 사용합니다.
 */
@Slf4j
@Component
public class LocalCacheManager {

    private final Cache<String, Object> cache;

    public LocalCacheManager(CacheProps cacheProps) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheProps.expirationMinutes(), TimeUnit.MINUTES)
                .maximumSize(cacheProps.maximumEntrySize())
                .build();
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        try {
            return Optional.ofNullable(cache.getIfPresent(key))
                    .map(clazz::cast);
        } catch (Exception e) {
            log.error("Failed to get cache value for key: {}", key, e);
            return Optional.empty();
        }
    }

    public void set(String key, Object value) {
        try {
            cache.put(key, value);
        } catch (Exception e) {
            log.error("Failed to set cache value for key: {}", key, e);
        }
    }

    public <T> T getOrElse(String key, Class<T> clazz, Supplier<T> callback) {
        try {
            return get(key, clazz).orElseGet(() -> {
                T value = callback.get();
                set(key, value);
                return value;
            });
        } catch (Exception e) {
            log.error("Failed to get or set cache value for key: {}", key, e);
            return callback.get();
        }
    }

    public void remove(String key) {
        try {
            cache.invalidate(key);
        } catch (Exception e) {
            log.error("Failed to delete cache for key: {}", key, e);
        }
    }
}
