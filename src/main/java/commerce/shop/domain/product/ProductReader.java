package commerce.shop.domain.product;

import commerce.shop.domain.category.Category;
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
                .orElseThrow(() -> new RuntimeException());
    }

    public List<ProductPriceSummary> readAllPriceSummaries() {
        return productRepository.findAllProductPricesGroupByBrandAndCategory();
    }

    public List<ProductPriceSummary> readPriceSummaries(Category category) {
        return productRepository.findAllProductPricesGroupByBrandAndCategory(category);
    }

    public boolean exists(long brandId) {
        Optional<Product> product = productRepository.findLatestByBrandId(brandId);

        return product.isPresent();
    }
}
