package commerce.shop.global.exception;

import commerce.shop.api.model.ApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        log.error("validation error occur. (message: {})", e.getMessage(), e);

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElseGet(ApiExceptionCode.DOMAIN_VALIDATION_FAILED::getMessage);

        return ApiResponse.<Void>builder()
                .code(ApiExceptionCode.DOMAIN_VALIDATION_FAILED)
                .message(message)
                .build()
                .toResponseEntity();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(HttpMessageNotReadableException e) {
        log.error("invalid request format error occur. (message: {})", e.getMessage(), e);
        return ApiResponse.fail(ApiExceptionCode.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException e) {
        log.error("illegal state exception occur. (message: {})", e.getMessage(), e);
        return ApiResponse.fail(ApiExceptionCode.INTERNAL_SERVER_ERROR).toResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("illegal argument exception occur. (message: {})", e.getMessage(), e);
        return ApiResponse.fail(ApiExceptionCode.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException e) {
        log.error("domain exception occur. (message: {})", e.getMessage(), e);
        return ApiResponse.fail(e.getCode()).toResponseEntity();
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerException(InternalServerException e) {
        log.error("internal server exception occur. (message: {})", e.getMessage(), e);
        return ApiResponse.fail(e.getCode()).toResponseEntity();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("internal server error occur. (message: {})", e.getMessage(), e);
        return ApiResponse.fail(ApiExceptionCode.INTERNAL_SERVER_ERROR).toResponseEntity();
    }
}

