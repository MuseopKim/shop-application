package commerce.shop.global.exception;

import commerce.shop.api.model.ApiResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionCode implements ApiResponseCode {

    PRODUCT_NOT_EXIST("존재하지 않는 상품입니다.", HttpStatus.BAD_REQUEST),

    BRAND_NOT_EXIST("존재하지 않는 브랜드입니다.", HttpStatus.BAD_REQUEST),
    BRAND_UNABLE_TO_REMOVE("삭제할 수 없는 브랜드입니다. 상품이 남아있는지 확인 해주세요.", HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_EXIST("존재하지 않는 카테고리입니다.", HttpStatus.BAD_REQUEST),

    DOMAIN_VALIDATION_FAILED("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("잚못 된 요청입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
