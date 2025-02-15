package commerce.shop.global.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.caffeine")
public record CacheProps(
        long maximumEntrySize,
        long expirationMinutes
) {

}
