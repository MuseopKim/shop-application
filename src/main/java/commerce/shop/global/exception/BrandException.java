package commerce.shop.global.exception;

import commerce.shop.api.model.ApiResponseCode;

public class BrandException extends DomainException {

    public BrandException(ApiResponseCode code) {
        super(code);
    }
}
