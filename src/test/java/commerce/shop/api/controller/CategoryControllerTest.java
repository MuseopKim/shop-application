package commerce.shop.api.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.api.controller.model.CategoryPriceRangePayload;
import commerce.shop.application.service.ProductPriceService;
import commerce.shop.application.service.model.PriceWithBrand;
import commerce.shop.application.service.model.PriceWithCategoryAndBrand;
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
        CategoryMinimumPricesPayload expectedPayload = CategoryMinimumPricesPayload.builder()
                .prices(List.of(
                        PriceWithCategoryAndBrand.builder()
                                .category(Category.TOP)
                                .brandName("C")
                                .price(10000)
                                .build(),
                        PriceWithCategoryAndBrand.builder()
                                .category(Category.OUTER)
                                .brandName("E")
                                .price(5000)
                                .build()
                ))
                .totalPrice(15000)
                .build();

        given(productPriceService.retrieveCategoryMinimumPrices())
                .willReturn(expectedPayload);

        // then
        mockMvc.perform(get("/categories/minimum-prices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.prices[0].category").value(Category.TOP.name()))
                .andExpect(jsonPath("$.payload.prices[0].brandName").value("C"))
                .andExpect(jsonPath("$.payload.prices[0].price").value(10000))
                .andExpect(jsonPath("$.payload.prices[1].category").value(Category.OUTER.name()))
                .andExpect(jsonPath("$.payload.prices[1].brandName").value("E"))
                .andExpect(jsonPath("$.payload.prices[1].price").value(5000))
                .andExpect(jsonPath("$.payload.totalPrice").value(15000));
    }

    @DisplayName("카테고리 별 최저 / 최고 가격 범위 조회 API")
    @Test
    void categoryPriceRangesTest() throws Exception {
        // given
        Category category = Category.TOP;

        CategoryPriceRangePayload expectedPayload = CategoryPriceRangePayload.builder()
                .category(category)
                .minimumPrices(List.of(
                        PriceWithBrand.builder()
                                .brandName("C")
                                .price(10000)
                                .build()
                ))
                .maximumPrices(List.of(
                        PriceWithBrand.builder()
                                .brandName("I")
                                .price(11400)
                                .build()
                ))
                .build();

        given(productPriceService.retrieveCategoryPriceRanges(category))
                .willReturn(expectedPayload);

        // then
        mockMvc.perform(get("/categories/{category}/price-ranges", category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.category").value(category.name()))
                .andExpect(jsonPath("$.payload.minimumPrices[0].brandName").value("C"))
                .andExpect(jsonPath("$.payload.minimumPrices[0].price").value(10000))
                .andExpect(jsonPath("$.payload.maximumPrices[0].brandName").value("I"))
                .andExpect(jsonPath("$.payload.maximumPrices[0].price").value(11400));
    }
}
