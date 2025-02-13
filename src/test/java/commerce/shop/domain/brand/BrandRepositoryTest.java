package commerce.shop.domain.brand;

import static org.assertj.core.api.Assertions.assertThatCode;

import commerce.shop.fixture.Fixtures;
import commerce.shop.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BrandRepositoryTest extends DataJpaTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void schemaTest() {
        assertThatCode(() -> brandRepository.save(Fixtures.brand().build())).doesNotThrowAnyException();
    }
}
