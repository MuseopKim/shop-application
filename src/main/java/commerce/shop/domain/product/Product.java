package commerce.shop.domain.product;

import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.category.Category;
import commerce.shop.global.jpa.DateTimeAuditEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends DateTimeAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private int price;

    public Product update(String name, Brand brand, Category category, int price) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;

        return this;
    }
}
