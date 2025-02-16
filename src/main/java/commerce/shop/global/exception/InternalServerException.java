package commerce.shop.global.exception;


import commerce.shop.api.model.ApiResponseCode;

public class InternalServerException extends DomainException {

    public InternalServerException(ApiResponseCode code) {
        super(code);
    }
}
