package commerce.shop.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final ApiResponseCode code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T payload;

    public static <T> ApiResponse<T> success(T payload) {
        return ApiResponse.<T>builder()
                .code(Code.SUCCESS)
                .message(Code.SUCCESS.getMessage())
                .payload(payload)
                .build();
    }

    public static <T> ApiResponse<T> success(ApiResponseCode code, T payload) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(code.getMessage())
                .payload(payload)
                .build();
    }

    public static ApiResponse<Void> fail(ApiResponseCode code) {
        return ApiResponse.<Void>builder()
                .code(code)
                .message(code.getMessage())
                .build();
    }

    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(this);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Code implements ApiResponseCode {
        SUCCESS("성공", HttpStatus.OK);

        private final String message;
        private final HttpStatus httpStatus;
    }
}
