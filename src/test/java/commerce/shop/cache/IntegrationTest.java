package commerce.shop.cache;

import commerce.shop.application.component.cache.CacheKey;
import commerce.shop.application.component.cache.CacheStorage;
import commerce.shop.domain.category.Category;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    protected CacheStorage cacheStorage;

    @AfterTestClass
    public void tearDown() {
        List<String> categoryKeys = EnumSet.allOf(Category.class)
                .stream()
                .map(category -> String.format(CacheKey.CATEGORY_PRICE_RANGE, category.name()))
                .toList();

        Stream.concat(Stream.of(CacheKey.CATEGORY_PRICE_MINIMUM, CacheKey.BRAND_PRICE_MINIMUM_TOTAL), categoryKeys.stream())
                .forEach(key -> cacheStorage.evict(key));
    }
}
