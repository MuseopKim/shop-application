package commerce.shop.domain.product;

import static commerce.shop.fixture.Fixtures.brand;
import static commerce.shop.fixture.Fixtures.product;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.category.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductWriterTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductWriter productWriter;

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
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

        when(productRepository.save(any(Product.class))).thenReturn(givenProduct);

        // when
        Product product = productWriter.create(givenBrand, command);
        Brand brand = product.getBrand();

        // then
        then(product.getId()).isEqualTo(givenProduct.getId());
        then(product.getName()).isEqualTo(givenProduct.getName());
        then(product.getCategory()).isEqualTo(givenProduct.getCategory());
        then(product.getPrice()).isEqualTo(givenProduct.getPrice());

        then(brand.getId()).isEqualTo(givenBrand.getId());
        then(brand.getName()).isEqualTo(givenBrand.getName());
    }

    @Test
    @DisplayName("상품을 수정한다")
    void update() {
        // given
        Brand givenBrand = brand().id(1L).name("나이키").build();
        Product existingProduct = product()
                .id(1L)
                .brand(givenBrand)
                .category(Category.TOP)
                .name("티셔츠")
                .price(10000)
                .build();

        ProductMutationCommand command =
                new ProductMutationCommand(givenBrand.getId(), Category.TOP, "프리미엄 티셔츠", 10000);

        when(productRepository.findById(existingProduct.getId())).thenReturn(Optional.of(existingProduct));

        // when
        Product product = productWriter.update(existingProduct.getId(), givenBrand, command);

        // then
        then(product.getId()).isEqualTo(existingProduct.getId());
        then(product.getCategory()).isEqualTo(command.category());
        then(product.getName()).isEqualTo(command.name());
        then(product.getPrice()).isEqualTo(command.price());
    }

    @DisplayName("상품 삭제 성공")
    @Test
    void delete() {
        // given
        long productId = 1L;
        Product product = product().build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        boolean result = productWriter.delete(productId);

        // then
        then(result).isTrue();
    }
}
