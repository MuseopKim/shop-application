package commerce.shop.domain.product;

import static commerce.shop.fixture.Fixtures.product;
import static org.assertj.core.api.Assertions.assertThatCode;

import commerce.shop.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductRepositoryTest extends DataJpaTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void schemaTest() {
        assertThatCode(() -> productRepository.save(product().build())).doesNotThrowAnyException();
    }
}
