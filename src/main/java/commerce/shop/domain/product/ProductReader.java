package commerce.shop.domain.product;

import commerce.shop.domain.category.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductReader {

    private final ProductRepository productRepository;

    public List<ProductPriceSummary> readAllPriceSummaries() {
        return productRepository.findAllProductPricesGroupByBrandAndCategory();
    }

    public List<ProductPriceSummary> readPriceSummaries(Category category) {
        return productRepository.findAllProductPricesGroupByBrandAndCategory(category);
    }
}
