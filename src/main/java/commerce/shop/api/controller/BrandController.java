package commerce.shop.api.controller;

import commerce.shop.api.controller.model.BrandMutationRequest;
import commerce.shop.api.model.ApiResponse;
import commerce.shop.application.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final ProductPriceService productPriceService;

    @GetMapping("/minimum-total-price")
    public ResponseEntity<?> minimumTotalPrices() {
        return ApiResponse.success(productPriceService.retrieveBrandMinimumTotalPrice()).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<?> registerBrand(@RequestBody BrandMutationRequest request) {
        return null;
    }
}
