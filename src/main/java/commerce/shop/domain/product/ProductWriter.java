package commerce.shop.domain.product;

import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.brand.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductWriter {

    private final ProductRepository productRepository;

    public Product create(Brand brand, ProductMutationCommand command) {
        return productRepository.save(Product.builder()
                .brand(brand)
                .category(command.category())
                .name(command.name())
                .price(command.price())
                .build());
    }
}
