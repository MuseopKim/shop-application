package commerce.shop.domain.product;

import static commerce.shop.fixture.Fixtures.brand;
import static commerce.shop.fixture.Fixtures.product;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.BDDAssertions.then;

import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandRepository;
import commerce.shop.domain.category.Category;
import commerce.shop.jpa.DataJpaTest;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductRepositoryTest extends DataJpaTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void schemaTest() {
        assertThatCode(() -> productRepository.save(product().build())).doesNotThrowAnyException();
    }

    @Test
    void findAllProductPricesGroupByBrandAndCategoryTest() {
        Brand brand = brandRepository.save(brand().build());

        Product maximumPriceProduct = productRepository.save(product()
                .category(Category.OUTER)
                .brand(brand)
                .price(10000)
                .build());

        Product minimumPriceProduct = productRepository.save(product()
                .category(Category.OUTER)
                .brand(brand)
                .price(1000)
                .build());

        ProductPriceSummary summary = productRepository.findAllProductPricesGroupByBrandAndCategory()
                .stream()
                .filter(price -> Objects.equals(price.brandId(), brand.getId()))
                .findFirst()
                .orElseThrow();

        then(summary.category()).isEqualTo(Category.OUTER);
        then(summary.maximumPrice()).isEqualTo(maximumPriceProduct.getPrice());
        then(summary.minimumPrice()).isEqualTo(minimumPriceProduct.getPrice());
    }
}
