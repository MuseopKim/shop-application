package commerce.shop.application.service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandPrice {

    private final String brandName;
    private final int price;
}
