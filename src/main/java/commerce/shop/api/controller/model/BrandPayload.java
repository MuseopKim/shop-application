package commerce.shop.api.controller.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandPayload {

    private long id;
    private String name;
}
