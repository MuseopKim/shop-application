package commerce.shop.domain.brand;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import commerce.shop.application.service.model.BrandMutationCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandWriterTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandWriter brandWriter;

    @DisplayName("브랜드를 생성한다")
    @Test
    void createTest() {
        // given
        BrandMutationCommand command = new BrandMutationCommand("나이키");
        Brand givenBrand = brand()
                .id(1L)
                .name("나이키")
                .build();

        when(brandRepository.save(any(Brand.class))).thenReturn(givenBrand);

        // when
        Brand brand = brandWriter.create(command);

        // then
        then(brand.getId()).isEqualTo(1L);
        then(brand.getName()).isEqualTo("나이키");
    }
}
