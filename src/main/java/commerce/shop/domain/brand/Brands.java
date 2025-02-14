package commerce.shop.domain.brand;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Brands {

    private final Map<Long, Brand> brands;

    private Brands(List<Brand> brands) {
        this.brands = brands.stream()
                .collect(Collectors.toMap(Brand::getId, Function.identity()));
    }

    public static Brands of(List<Brand> brands) {
        return new Brands(brands);
    }

    public Optional<String> findNameById(long id) {
        return Optional.ofNullable(brands.get(id))
                .map(Brand::getName);
    }

    public int size() {
        return brands.size();
    }
}
