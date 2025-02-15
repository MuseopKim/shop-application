package commerce.shop.application.service.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryMinimumPrices {

    private final List<CategoryBrandPrice> prices;
    private final int totalPrice;
}
