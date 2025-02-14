package commerce.shop.fixture;

import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.Product;

public class Fixtures {

    public static Brand.BrandBuilder brand() {
        return Brand.builder()
                .name("brand fixture");
    }

    public static Product.ProductBuilder product() {
        return Product.builder()
                .brand(brand().id(1L).build())
                .category(Category.TOP)
                .name("product fixture")
                .price(10_000);
    }
}
