package commerce.shop.global.exception;

import commerce.shop.api.model.ApiResponseCode;

public class CategoryException extends DomainException {

    public CategoryException(ApiResponseCode code) {
        super(code);
    }
}
