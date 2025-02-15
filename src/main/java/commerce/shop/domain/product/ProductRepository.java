package commerce.shop.domain.product;

import commerce.shop.domain.category.Category;
import java.util.List;
import java.util.Optional;
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
            """)
    List<ProductPriceSummary> findAllProductPricesGroupByBrandAndCategory();

    @Query("""
             SELECT
             new commerce.shop.domain.product.ProductPriceSummary(
                 p.category,
                 p.brand.id,
                 MIN(p.price),
                 MAX(p.price))
             FROM Product p
             WHERE p.category = :category
             GROUP BY p.category, p.brand
            """)
    List<ProductPriceSummary> findAllProductPricesGroupByBrandAndCategory(Category category);

    @Query("""
              SELECT p
              FROM Product p
              WHERE p.brand.id = :brandId
              ORDER BY p.id DESC
              LIMIT 1
            """)
    Optional<Product> findLatestByBrandId(long brandId);
}
