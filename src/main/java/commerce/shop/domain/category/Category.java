package commerce.shop.domain.category;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    TOP("상의"),
    OUTER("아우터"),
    PANTS("바지"),
    SNEAKERS("스니커즈"),
    BAG("가방"),
    CAP("모자"),
    SOCKS("양말"),
    ACCESSORIES("액세서리");

    private final String description;

    public static Optional<Category> from(String value) {
        if (Objects.isNull(value)) {
            return Optional.empty();
        }

        return Arrays.stream(Category.values())
                .filter(category -> Objects.equals(category.name(), value.toUpperCase()))
                .findFirst();
    }
}
