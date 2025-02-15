package commerce.shop.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import commerce.shop.api.controller.model.ProductMutationRequest;
import commerce.shop.api.controller.model.ProductPayload;
import commerce.shop.application.service.ProductService;
import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @DisplayName("상품 등록 API")
    @Test
    void registerProductTest() throws Exception {
        // given
        ProductMutationRequest request = new ProductMutationRequest(1L, Category.TOP, "기본 티셔츠", 10000);
        ProductPayload expectedResponse = ProductPayload.builder()
                .id(1L)
                .brandId(request.brandId())
                .brandName("나이키")
                .category(request.category())
                .name(request.name())
                .price(request.price())
                .build();

        when(productService.registerProduct(any(ProductMutationCommand.class)))
                .thenReturn(expectedResponse);

        // when / then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.payload.brandId").value(expectedResponse.getBrandId()))
                .andExpect(jsonPath("$.payload.brandName").value(expectedResponse.getBrandName()))
                .andExpect(jsonPath("$.payload.category").value(expectedResponse.getCategory().name()))
                .andExpect(jsonPath("$.payload.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.payload.price").value(expectedResponse.getPrice()));
    }

    @DisplayName("상품 수정 API")
    @Test
    void modifyProductTest() throws Exception {
        // given
        long productId = 1L;
        ProductMutationRequest request = new ProductMutationRequest(1L, Category.TOP, "프리미엄 티셔츠", 20000);
        ProductPayload expectedResponse = ProductPayload.builder()
                .id(1L)
                .brandId(request.brandId())
                .brandName("나이키")
                .category(request.category())
                .name(request.name())
                .price(request.price())
                .build();

        when(productService.modifyProduct(eq(productId), any(ProductMutationCommand.class)))
                .thenReturn(expectedResponse);

        // when / then
        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.payload.brandId").value(expectedResponse.getBrandId()))
                .andExpect(jsonPath("$.payload.brandName").value(expectedResponse.getBrandName()))
                .andExpect(jsonPath("$.payload.category").value(expectedResponse.getCategory().name()))
                .andExpect(jsonPath("$.payload.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.payload.price").value(expectedResponse.getPrice()));
    }

    @DisplayName("상품 삭제 API")
    @Test
    void removeProductTest() throws Exception {
        // given
        long productId = 1L;

        when(productService.removeProduct(productId)).thenReturn(true);

        // when / then
        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(true));
    }
}
