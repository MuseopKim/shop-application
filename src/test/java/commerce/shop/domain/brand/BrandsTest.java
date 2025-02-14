package commerce.shop.domain.brand;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BrandsTest {

    @Test
    void findNameByIdTest() {
        // given
        List<Brand> givenBrands = List.of(
                brand().id(1L).name("브랜드A").build(),
                brand().id(2L).name("브랜드B").build()
        );

        Brands brands = Brands.of(givenBrands);

        // when
        Optional<String> brandName = brands.findNameById(1L);

        // then
        then(brandName)
                .isPresent()
                .contains("브랜드A");
    }
}
