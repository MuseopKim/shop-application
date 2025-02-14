package commerce.shop.domain.product;

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
}
