package commerce.shop.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import commerce.shop.api.controller.model.BrandMinimumTotalPricePayload;
import commerce.shop.application.service.ProductPriceService;
import commerce.shop.application.service.model.BrandTotalPrice;
import commerce.shop.application.service.model.CategoryPrice;
import commerce.shop.domain.category.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductPriceService productPriceService;

    @DisplayName("브랜드 별 최저가 조회 API 테스트")
    @Test
    void minimumTotalPricesTest() throws Exception {
        // given
        List<CategoryPrice> categories = List.of(
                CategoryPrice.builder()
                        .category(Category.TOP)
                        .price(10000)
                        .build(),
                CategoryPrice.builder()
                        .category(Category.OUTER)
                        .price(5000)
                        .build()
        );

        BrandTotalPrice brandTotalPrice = BrandTotalPrice.builder()
                .brandName("브랜드D")
                .categories(categories)
                .totalPrice(15000)
                .build();

        BrandMinimumTotalPricePayload response = BrandMinimumTotalPricePayload.builder()
                .minimumPrice(brandTotalPrice)
                .build();

        when(productPriceService.retrieveBrandMinimumTotalPrice()).thenReturn(response);

        // when / then
        mockMvc.perform(get("/brands/minimum-total-price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.payload.minimumPrice.brandName").value("브랜드D"))
                .andExpect(jsonPath("$.payload.minimumPrice.totalPrice").value(15000))
                .andExpect(jsonPath("$.payload.minimumPrice.categories", hasSize(2)))
                .andExpect(jsonPath("$.payload.minimumPrice.categories[0].category").value("TOP"))
                .andExpect(jsonPath("$.payload.minimumPrice.categories[0].price").value(10000))
                .andExpect(jsonPath("$.payload.minimumPrice.categories[1].category").value("OUTER"))
                .andExpect(jsonPath("$.payload.minimumPrice.categories[1].price").value(5000))
                .andDo(print());
    }
}
