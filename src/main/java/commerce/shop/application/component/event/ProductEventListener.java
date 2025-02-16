package commerce.shop.application.component.event;

import commerce.shop.application.component.cache.CacheKey;
import commerce.shop.application.component.cache.CacheStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ProductEventListener {

    private final CacheStorage cacheStorage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductEvent event) {
        // category:price:range:{categoryName}
        cacheStorage.evict(String.format(CacheKey.CATEGORY_PRICE_RANGE, event.getCategory().name()));
        cacheStorage.evict(CacheKey.BRAND_PRICE_MINIMUM_TOTAL);
        cacheStorage.evict(CacheKey.CATEGORY_PRICE_MINIMUM);
    }
}
