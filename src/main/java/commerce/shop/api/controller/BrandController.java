package commerce.shop.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @GetMapping("/minimum-total-price")
    public ResponseEntity<?> minimumTotalPrices() {
        return null;
    }
}
