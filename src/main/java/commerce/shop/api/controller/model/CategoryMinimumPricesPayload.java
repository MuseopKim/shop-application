package commerce.shop.api.controller.model;

import commerce.shop.application.service.model.CategoryBrandPrice;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryMinimumPricesPayload {

    private final List<CategoryBrandPrice> prices;
    private final int totalPrice;
}
