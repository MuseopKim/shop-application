package commerce.shop.api.controller;

import commerce.shop.api.model.ApiResponse;
import commerce.shop.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final ProductService productService;

    @GetMapping("/minimum-total-price")
    public ResponseEntity<?> minimumTotalPrices() {
        return ApiResponse.success(productService.retrieveBrandMinimumTotalPrice()).toResponseEntity();
    }
}
