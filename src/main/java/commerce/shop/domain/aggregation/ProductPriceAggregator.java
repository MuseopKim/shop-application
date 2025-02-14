package commerce.shop.domain.aggregation;

import commerce.shop.domain.aggregation.model.CategoryPriceAggregation;
import commerce.shop.domain.aggregation.model.PriceInformation;
import commerce.shop.domain.product.Category;
import commerce.shop.domain.product.ProductPriceSummary;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceAggregator {

    public CategoryPriceAggregation aggregateMinimumPriceByCategory(Collection<ProductPriceSummary> prices) {
        Map<Category, PriceInformation> minimumPricesByCategory = prices.stream()
                .collect(Collectors.toMap(
                        ProductPriceSummary::category,
                        summary -> new PriceInformation(summary.brandId(), summary.minimumPrice()),
                        (one, other) -> one.price() <= other.price() ? one : other));

        return new CategoryPriceAggregation(minimumPricesByCategory);
    }
}
