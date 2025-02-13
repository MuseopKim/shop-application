package commerce.shop.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
             SELECT
             new commerce.shop.domain.product.ProductPriceSummary(
                 p.category,
                 p.brand.id,
                 MIN(p.price),
                 MAX(p.price))
             FROM Product p
             GROUP BY p.category, p.brand
            \s""")
    List<ProductPriceSummary> findAllProductPricesGroupByBrandAndCategory();
}
