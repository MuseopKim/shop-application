package commerce.shop.application.service;

import static commerce.shop.fixture.Fixtures.brand;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.Mockito.when;

import commerce.shop.api.controller.model.BrandPayload;
import commerce.shop.application.service.model.BrandMutationCommand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandWriter;
import commerce.shop.domain.product.ProductReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandWriter brandWriter;

    @Mock
    private ProductReader productReader;

    @InjectMocks
    private BrandService brandService;

    @DisplayName("브랜드를 등록한다")
    @Test
    void registerBrandTest() {
        // given
        BrandMutationCommand command = new BrandMutationCommand("나이키");
        Brand givenBrand = brand()
                .id(1L)
                .name("나이키")
                .build();

        when(brandWriter.create(command)).thenReturn(givenBrand);

        // when
        BrandPayload brand = brandService.registerBrand(command);

        // then
        then(brand.getId()).isEqualTo(givenBrand.getId());
        then(brand.getName()).isEqualTo(givenBrand.getName());
    }

    @DisplayName("브랜드를 수정한다")
    @Test
    void modifyBrandTest() {
        // given
        long brandId = 1L;
        BrandMutationCommand command = new BrandMutationCommand("나이키");

        Brand givenBrand = brand().id(brandId).name("나이키").build();
        when(brandWriter.update(brandId, command)).thenReturn(givenBrand);

        // when
        BrandPayload brand = brandService.modifyBrand(brandId, command);

        // then
        then(brand.getId()).isEqualTo(givenBrand.getId());
        then(brand.getName()).isEqualTo(givenBrand.getName());
    }

    @DisplayName("브랜드 삭제 - 성공")
    @Test
    void removeBrandTest() {
        // given
        long brandId = 1L;

        when(productReader.exists(brandId)).thenReturn(false);
        when(brandWriter.delete(brandId)).thenReturn(true);

        // when
        boolean removed = brandService.removeBrand(brandId);

        // then
        then(removed).isTrue();
    }

    @DisplayName("브랜드 삭제 - 연관된 상품이 있는 경우")
    @Test
    void removeBrandWhenProductExistsTest() {
        // given
        long brandId = 1L;

        when(productReader.exists(brandId)).thenReturn(true);

        // when / then
        thenThrownBy(() -> brandService.removeBrand(brandId))
                .isInstanceOf(RuntimeException.class);
    }
}
