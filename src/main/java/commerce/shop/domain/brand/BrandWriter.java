package commerce.shop.domain.brand;

import commerce.shop.application.service.model.BrandMutationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandWriter {

    private final BrandRepository brandRepository;

    public Brand create(BrandMutationCommand command) {
        return brandRepository.save(Brand.builder()
                .name(command.name())
                .build());
    }
}
