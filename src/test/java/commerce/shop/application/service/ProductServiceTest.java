package commerce.shop.application.service;

import static commerce.shop.fixture.Fixtures.brand;
import static commerce.shop.fixture.Fixtures.product;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import commerce.shop.api.controller.model.ProductPayload;
import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.Product;
import commerce.shop.domain.product.ProductWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductWriter productWriter;

    @Mock
    private BrandReader brandReader;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다")
    @Test
    void registerProductTest() {
        // given
        Brand givenBrand = brand().id(1L).name("나이키").build();
        ProductMutationCommand command = new ProductMutationCommand(givenBrand.getId(), Category.TOP, "티셔츠", 10000);
        Product givenProduct = product()
                .id(1L)
                .brand(givenBrand)
                .category(Category.TOP)
                .name("티셔츠")
                .price(10000)
                .build();

        when(brandReader.getById(command.brandId())).thenReturn(givenBrand);
        when(productWriter.create(givenBrand, command)).thenReturn(givenProduct);

        // when
        ProductPayload payload = productService.registerProduct(command);

        // then
        then(payload.getId()).isEqualTo(givenProduct.getId());
        then(payload.getBrandId()).isEqualTo(givenBrand.getId());
        then(payload.getBrandName()).isEqualTo(givenBrand.getName());
        then(payload.getCategory()).isEqualTo(givenProduct.getCategory());
        then(payload.getName()).isEqualTo(givenProduct.getName());
        then(payload.getPrice()).isEqualTo(givenProduct.getPrice());
    }

    @DisplayName("상품을 수정한다")
    @Test
    void update() {
        // given
        Brand givenBrand = brand().id(1L).name("나이키").build();
        ProductMutationCommand command = new ProductMutationCommand(givenBrand.getId(), Category.TOP, "티셔츠", 10000);
        Product existingProduct = product()
                .id(1L)
                .brand(givenBrand)
                .category(Category.TOP)
                .name("티셔츠")
                .price(10000)
                .build();

        Product updatedProduct = product()
                .id(1L)
                .brand(givenBrand)
                .category(Category.TOP)
                .name("프리미엄 티셔츠")
                .price(20000)
                .build();

        when(brandReader.getById(command.brandId())).thenReturn(givenBrand);
        when(productWriter.update(any(Long.class), any(Brand.class), any(ProductMutationCommand.class)))
                .thenReturn(updatedProduct);

        // when
        ProductPayload payload = productService.modifyProduct(existingProduct.getId(), command);

        // then
        then(payload.getId()).isEqualTo(updatedProduct.getId());
        then(payload.getBrandId()).isEqualTo(givenBrand.getId());
        then(payload.getBrandName()).isEqualTo(givenBrand.getName());
        then(payload.getCategory()).isEqualTo(updatedProduct.getCategory());
        then(payload.getName()).isEqualTo(updatedProduct.getName());
        then(payload.getPrice()).isEqualTo(updatedProduct.getPrice());
    }

    @DisplayName("상품을 삭제한다")
    @Test
    void removeProduct() {
        // given
        long productId = 1L;
        when(productWriter.delete(productId)).thenReturn(true);

        // when
        boolean result = productService.removeProduct(productId);

        // then
        then(result).isTrue();
    }
}
