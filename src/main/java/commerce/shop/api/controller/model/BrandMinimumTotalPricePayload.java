package commerce.shop.api.controller.model;

import commerce.shop.application.service.model.BrandTotalPrice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandMinimumTotalPricePayload {

    private final BrandTotalPrice minimumPrice;
}
