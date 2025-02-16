package commerce.shop.application.service;

import commerce.shop.api.controller.model.ProductPayload;
import commerce.shop.application.component.event.ProductEvent;
import commerce.shop.application.component.event.ProductEventPublisher;
import commerce.shop.application.component.event.ProductEventType;
import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.product.Product;
import commerce.shop.domain.product.ProductReader;
import commerce.shop.domain.product.ProductWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductWriter productWriter;
    private final BrandReader brandReader;
    private final ProductReader productReader;
    private final ProductEventPublisher eventPublisher;

    @Transactional
    public ProductPayload registerProduct(ProductMutationCommand command) {
        Brand brand = brandReader.getById(command.brandId());

        Product product = productWriter.create(brand, command);

        eventPublisher.publishEvictEvent(ProductEvent.of(ProductEventType.PRODUCT_CREATED, product));

        return ProductPayload.of(product, brand);
    }

    @Transactional
    public ProductPayload modifyProduct(long id, ProductMutationCommand command) {
        Brand brand = brandReader.getById(command.brandId());

        Product product = productWriter.update(id, brand, command);

        eventPublisher.publishEvictEvent(ProductEvent.of(ProductEventType.PRODUCT_UPDATED, product));

        return ProductPayload.of(product, brand);
    }

    @Transactional
    public boolean removeProduct(long id) {
        Product product = productReader.getById(id);

        boolean productDeleted = productWriter.delete(id);

        if (productDeleted) {
            eventPublisher.publishEvictEvent(ProductEvent.of(ProductEventType.PRODUCT_DELETED, product));
        }

        return productDeleted;
    }
}
