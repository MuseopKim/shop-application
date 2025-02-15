package commerce.shop.domain.brand;

import commerce.shop.application.service.model.BrandMutationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class BrandWriter {

    private final BrandRepository brandRepository;

    @Transactional
    public Brand create(BrandMutationCommand command) {
        return brandRepository.save(Brand.builder()
                .name(command.name())
                .build());
    }

    @Transactional
    public Brand update(long id, BrandMutationCommand command) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        return brand.update(command.name());
    }
}
