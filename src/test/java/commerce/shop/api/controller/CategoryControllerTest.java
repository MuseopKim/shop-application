package commerce.shop.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import commerce.shop.application.service.model.CategoryBrandPrice;
import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.application.service.ProductPriceService;
import commerce.shop.domain.category.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductPriceService productPriceService;

    @DisplayName("카테고리 별 최저 가격 조회")
    @Test
    void categoryMinimumPricesTest() throws Exception {
        // given
        CategoryMinimumPricesPayload expectedResponse = CategoryMinimumPricesPayload.builder()
                .prices(List.of(
                        CategoryBrandPrice.builder()
                                .category(Category.TOP)
                                .brandName("C")
                                .price(10000)
                                .build(),
                        CategoryBrandPrice.builder()
                                .category(Category.OUTER)
                                .brandName("E")
                                .price(5000)
                                .build()
                ))
                .totalPrice(15000)
                .build();

        given(productPriceService.retrieveCategoryMinimumPrices())
                .willReturn(expectedResponse);

        // then
        mockMvc.perform(get("/categories/minimum-prices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.payload.prices[0].category").value(Category.TOP.name()))
                .andExpect(jsonPath("$.payload.prices[0].brandName").value("C"))
                .andExpect(jsonPath("$.payload.prices[0].price").value(10000))
                .andExpect(jsonPath("$.payload.prices[1].category").value(Category.OUTER.name()))
                .andExpect(jsonPath("$.payload.prices[1].brandName").value("E"))
                .andExpect(jsonPath("$.payload.prices[1].price").value(5000))
                .andExpect(jsonPath("$.payload.totalPrice").value(15000));
    }
}
