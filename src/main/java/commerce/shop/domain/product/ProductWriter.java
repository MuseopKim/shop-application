package commerce.shop.domain.product;

import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.global.exception.ApiExceptionCode;
import commerce.shop.global.exception.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ProductWriter {

    private final ProductRepository productRepository;

    @Transactional
    public Product create(Brand brand, ProductMutationCommand command) {
        return productRepository.save(Product.builder()
                .brand(brand)
                .category(command.category())
                .name(command.name())
                .price(command.price())
                .build());
    }

    @Transactional
    public Product update(long id, Brand brand, ProductMutationCommand command) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ApiExceptionCode.PRODUCT_NOT_EXIST));

        return product.update(command.name(), brand, command.category(), command.price());
    }

    @Transactional
    public boolean delete(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ApiExceptionCode.PRODUCT_NOT_EXIST));

        productRepository.delete(product);

        return true;
    }
}
