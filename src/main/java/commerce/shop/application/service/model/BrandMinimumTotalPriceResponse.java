package commerce.shop.application.service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandMinimumTotalPriceResponse {

    private final BrandTotalPrice minimumPrice;
}
