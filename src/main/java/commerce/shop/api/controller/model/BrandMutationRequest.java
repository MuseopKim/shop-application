package commerce.shop.api.controller.model;

import commerce.shop.application.service.model.BrandMutationCommand;
import commerce.shop.global.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BrandMutationRequest(

        @NotBlank(message = ValidationMessage.Brand.NAME_NOT_BLANK)
        @Size(max = 50, message = ValidationMessage.Brand.NAME_SIZE)
        String name
) {

    public BrandMutationCommand toCommand() {
        return new BrandMutationCommand(name);
    }
}
