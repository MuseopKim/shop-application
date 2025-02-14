package commerce.shop.api.controller;

import commerce.shop.application.service.ProductService;
import commerce.shop.domain.product.PriceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ProductService productService;

    @GetMapping("/minimum-prices")
    public ResponseEntity<?> categoryMinimumPrices() {
        productService.retrieveCategoryPrices(PriceType.MINIMUM_PRICE);
        return null;
    }
}
