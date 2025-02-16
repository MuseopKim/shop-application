package commerce.shop.global.exception;

import commerce.shop.api.model.ApiResponseCode;

public class ProductException extends DomainException {

    public ProductException(ApiResponseCode code) {
        super(code);
    }
}
