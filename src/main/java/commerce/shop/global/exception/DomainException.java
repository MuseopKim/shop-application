package commerce.shop.global.exception;

import commerce.shop.api.model.ApiResponseCode;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final ApiResponseCode code;

    public DomainException(ApiResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
