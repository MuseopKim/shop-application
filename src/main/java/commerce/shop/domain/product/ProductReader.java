package commerce.shop.domain.product;

import commerce.shop.domain.category.Category;
import commerce.shop.global.exception.ApiExceptionCode;
import commerce.shop.global.exception.ProductException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductReader {

    private final ProductRepository productRepository;

    public Product getById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ApiExceptionCode.PRODUCT_NOT_EXIST));
    }

    public List<ProductPriceSummary> readAllPriceSummaries() {
        return productRepository.findAllProductPricesGroupByBrandAndCategory();
    }

    public List<ProductPriceSummary> readPriceSummaries(Category category) {
        return productRepository.findAllProductPricesGroupByBrandAndCategory(category);
    }

    public boolean existsByBrandId(long brandId) {
        Optional<Product> product = productRepository.findLatestByBrandId(brandId);

        return product.isPresent();
    }
}
