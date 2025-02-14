package commerce.shop.domain.brand;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandReader {

    private final BrandRepository brandRepository;

    public Brands readBrands(Collection<Long> brandIds) {
        List<Brand> brands = brandRepository.findAllById(brandIds);

        return Brands.of(brands);
    }
}
