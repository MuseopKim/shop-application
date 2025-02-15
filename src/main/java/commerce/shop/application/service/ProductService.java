package commerce.shop.application.service;

import commerce.shop.api.controller.model.ProductPayload;
import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.product.Product;
import commerce.shop.domain.product.ProductWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductWriter productWriter;
    private final BrandReader brandReader;

    @Transactional
    public ProductPayload registerProduct(ProductMutationCommand command) {
        Brand brand = brandReader.getById(command.brandId());

        Product product = productWriter.create(brand, command);

        return ProductPayload.of(product, brand);
    }
}
