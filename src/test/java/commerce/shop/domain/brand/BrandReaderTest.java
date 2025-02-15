package commerce.shop.domain.brand;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandReaderTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandReader brandReader;

    @DisplayName("브랜드 ID 목록으로 브랜드 조회")
    @Test
    void readAllByIdsTest() {
        // given
        List<Long> brandIds = List.of(1L, 2L, 3L);
        List<Brand> givenBrands = List.of(
                brand().id(1L).name("브랜드A").build(),
                brand().id(2L).name("브랜드B").build(),
                brand().id(3L).name("브랜드C").build()
        );

        when(brandRepository.findAllById(brandIds))
                .thenReturn(givenBrands);

        // when
        Brands brands = brandReader.readAllByIds(brandIds);

        // then
        then(brands.size()).isEqualTo(givenBrands.size());
    }
}
