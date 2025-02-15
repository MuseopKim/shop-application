package commerce.shop.api.controller;

import commerce.shop.api.controller.model.ProductMutationRequest;
import commerce.shop.api.model.ApiResponse;
import commerce.shop.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> registerProduct(@RequestBody @Valid ProductMutationRequest request) {
        return ApiResponse.success(productService.registerProduct(request.toCommand())).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyProduct(@PathVariable long id, @RequestBody @Valid ProductMutationRequest request) {
        return ApiResponse.success(productService.modifyProduct(id, request.toCommand())).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable long id) {
        return ApiResponse.success(productService.removeProduct(id)).toResponseEntity();
    }
}
