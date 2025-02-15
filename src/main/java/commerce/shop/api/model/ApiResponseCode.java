package commerce.shop.api.model;

import org.springframework.http.HttpStatus;

public interface ApiResponseCode {

    String getMessage();

    HttpStatus getHttpStatus();
}
