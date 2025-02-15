package commerce.shop.api.controller;

import commerce.shop.api.controller.model.BrandMutationRequest;
import commerce.shop.api.model.ApiResponse;
import commerce.shop.application.service.BrandService;
import commerce.shop.application.service.ProductPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;
    private final ProductPriceService productPriceService;

    @GetMapping("/minimum-total-price")
    public ResponseEntity<?> minimumTotalPrices() {
        return ApiResponse.success(productPriceService.retrieveBrandMinimumTotalPrice()).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<?> registerBrand(@RequestBody @Valid BrandMutationRequest request) {
        return ApiResponse.success(brandService.registerBrand(request.toCommand())).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyBrand(
            @PathVariable long id,
            @RequestBody @Valid BrandMutationRequest request) {
        return ApiResponse.success(brandService.modifyBrand(id, request.toCommand())).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeBrand(@PathVariable long id) {
        return ApiResponse.success(brandService.removeBrand(id)).toResponseEntity();
    }
}
