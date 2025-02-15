package commerce.shop.application.service;

import commerce.shop.api.controller.model.CategoryPrice;
import commerce.shop.api.controller.model.CategoryPrices;
import commerce.shop.application.component.aggregation.ProductPriceAggregator;
import commerce.shop.application.component.aggregation.model.CategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.ProductPrice;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.brand.Brands;
import commerce.shop.domain.product.PriceType;
import commerce.shop.domain.product.ProductPriceSummary;
import commerce.shop.domain.product.ProductReader;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductReader productReader;
    private final BrandReader brandReader;
    private final ProductPriceAggregator priceAggregator;

    @Transactional(readOnly = true)
    public CategoryPrices retrieveCategoryMinimumPrices() {
        List<ProductPriceSummary> priceSummaries = productReader.readAllPriceSummaries();

        CategoryPriceAggregation aggregation = priceAggregator.aggregatePricesOfCategory(priceSummaries);

        List<ProductPrice> productPrices = aggregation.allPricesOf(PriceType.MINIMUM_PRICE);

        int totalPrice = aggregation.calculateTotalPrice(PriceType.MINIMUM_PRICE);

        Brands brands = brandReader.readAllByIds(productPrices.stream()
                .map(ProductPrice::brandId)
                .collect(Collectors.toSet()));

        List<CategoryPrice> categoryPrices = productPrices.stream()
                .map(productPrice -> CategoryPrice.builder()
                        .category(productPrice.category())
                        .brandName(brands.findNameById(productPrice.brandId())
                                .orElse("Unknown"))
                        .price(productPrice.price())
                        .build())
                .toList();

        return CategoryPrices.builder()
                .prices(categoryPrices)
                .totalPrice(totalPrice)
                .build();
    }
}
