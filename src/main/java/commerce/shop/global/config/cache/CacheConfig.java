package commerce.shop.global.config.cache;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(CacheProps.class)
@EnableCaching
@Configuration
public class CacheConfig {

}
