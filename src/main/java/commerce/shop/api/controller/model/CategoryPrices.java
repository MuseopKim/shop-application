package commerce.shop.api.controller.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryPrices {

    private final List<CategoryPrice> prices;
    private final int totalPrice;
}
