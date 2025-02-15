package commerce.shop.api.controller.model;

import commerce.shop.application.service.model.ProductMutationCommand;
import commerce.shop.domain.category.Category;
import commerce.shop.global.constant.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductMutationRequest(

        @NotNull(message = ValidationMessage.Product.BRAND_ID_NOT_NULL)
        Long brandId,

        @NotNull(message = ValidationMessage.Product.CATEGORY_NOT_BLANK)
        Category category,

        @NotBlank(message = ValidationMessage.Product.NAME_NOT_BLANK)
        @Size(max = 100, message = ValidationMessage.Product.NAME_SIZE)
        String name,

        @NotNull(message = ValidationMessage.Product.PRICE_NOT_NULL)
        @Min(value = 0, message = ValidationMessage.Product.PRICE_MIN)
        Integer price
) {

    public ProductMutationCommand toCommand() {
        return new ProductMutationCommand(brandId, category, name, price);
    }
}

