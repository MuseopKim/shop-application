package commerce.shop.application.service.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandTotalPrice {

    private final String brandName;
    private final List<CategoryPrice> categories;
    private final int totalPrice;
}
