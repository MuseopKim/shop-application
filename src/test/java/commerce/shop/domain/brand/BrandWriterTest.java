package commerce.shop.domain.brand;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import commerce.shop.application.service.model.BrandMutationCommand;
import java.util.Optional;
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

    @DisplayName("브랜드 수정 - 성공")
    @Test
    void updateBrandTest() {
        // given
        Long brandId = 1L;
        BrandMutationCommand command = new BrandMutationCommand("나이키");

        Brand existingBrand = brand().id(brandId).name("아디다스").build();
        Brand updatedBrand = brand().id(brandId).name("나이키").build();

        // when
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));

        Brand brand = brandWriter.update(brandId, command);

        // then
        then(brand.getId()).isEqualTo(existingBrand.getId());
        then(brand.getName()).isEqualTo(updatedBrand.getName());
    }

    @DisplayName("브랜드 수정 - 존재하지 않는 브랜드")
    @Test
    void updateBrandNotFoundTest() {
        // given
        long brandId = 1L;
        BrandMutationCommand command = new BrandMutationCommand("나이키");

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // when & then
        thenThrownBy(() -> brandWriter.update(brandId, command))
                .isInstanceOf(RuntimeException.class);
    }
}
