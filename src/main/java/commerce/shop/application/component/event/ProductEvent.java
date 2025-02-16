package commerce.shop.application.component.event;

import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductEvent {

    private ProductEventType type;
    private final long id;
    private final Category category;
    private final String name;
    private final int price;

    public static ProductEvent of(ProductEventType type, Product product) {
        return ProductEvent.builder()
                .type(type)
                .id(product.getId())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
