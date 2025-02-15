package commerce.shop.api.controller.model;

import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductPayload {

    private final long id;
    private final long brandId;
    private final String brandName;
    private final Category category;
    private final String name;
    private final int price;

    public static ProductPayload of(Product product, Brand brand) {
        return ProductPayload.builder()
                .id(product.getId())
                .brandId(brand.getId())
                .brandName(brand.getName())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
