package commerce.shop.api.controller;

import commerce.shop.api.model.ApiResponse;
import commerce.shop.application.service.ProductPriceService;
import commerce.shop.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ProductPriceService productPriceService;

    @GetMapping("/minimum-prices")
    public ResponseEntity<?> categoryMinimumPrices() {
        return ApiResponse.success(productPriceService.retrieveCategoryMinimumPrices())
                .toResponseEntity();
    }

    @GetMapping("/{category}/price-ranges")
    public ResponseEntity<?> categoryPriceRanges(@PathVariable Category category) {
        return ApiResponse.success(productPriceService.retrieveCategoryPriceRanges(category)).toResponseEntity();
    }
}
