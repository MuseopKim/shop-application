package commerce.shop.application.service;

import commerce.shop.application.service.model.BrandMutationCommand;
import commerce.shop.application.service.model.BrandPayload;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandWriter brandWriter;

    @Transactional
    public BrandPayload registerBrand(BrandMutationCommand command) {
        Brand brand = brandWriter.create(command);

        return BrandPayload.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
