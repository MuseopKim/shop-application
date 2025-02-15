package commerce.shop.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import commerce.shop.api.controller.model.BrandMutationRequest;
import commerce.shop.api.controller.model.BrandPayload;
import commerce.shop.application.service.BrandService;
import commerce.shop.application.service.ProductPriceService;
import commerce.shop.application.service.model.BrandMutationCommand;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductPriceService productPriceService;

    @MockitoBean
    private BrandService brandService;

    @Test
    @DisplayName("브랜드 등록 성공")
    void registerBrand() throws Exception {
        // given
        BrandMutationRequest request = new BrandMutationRequest("나이키");
        BrandPayload expectedResponse = BrandPayload.builder()
                .id(1L)
                .name("나이키")
                .build();

        when(brandService.registerBrand(any(BrandMutationCommand.class)))
                .thenReturn(expectedResponse);

        // when / then
        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.payload.name").value(expectedResponse.getName()));
    }

    @Test
    @DisplayName("브랜드 수정 API")
    void modifyBrand() throws Exception {
        // given
        long brandId = 1L;
        BrandMutationRequest request = new BrandMutationRequest("아디다스");
        BrandPayload expectedResponse = BrandPayload.builder()
                .id(brandId)
                .name("아디다스")
                .build();

        when(brandService.modifyBrand(eq(brandId), any(BrandMutationCommand.class)))
                .thenReturn(expectedResponse);

        // when / then
        mockMvc.perform(put("/brands/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id").value(brandId))
                .andExpect(jsonPath("$.payload.name").value(request.name()));
    }

    @Test
    @DisplayName("브랜드 삭제 API")
    void removeBrand() throws Exception {
        // given
        long brandId = 1L;
        when(brandService.removeBrand(brandId)).thenReturn(true);

        // when / then
        mockMvc.perform(delete("/brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(true));
    }
}
