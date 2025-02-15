package commerce.shop.global.converter;

import commerce.shop.domain.category.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        return Category.from(source)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category value: " + source));
    }
}
