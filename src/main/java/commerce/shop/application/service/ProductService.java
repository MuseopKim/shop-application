package commerce.shop.application.service;

import commerce.shop.application.component.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.application.service.model.*;
import commerce.shop.application.component.aggregation.ProductPriceAggregator;
import commerce.shop.application.component.aggregation.model.CategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.ProductPrice;
import commerce.shop.domain.brand.Brand;
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
    public CategoryMinimumPrices retrieveCategoryMinimumPrices() {
        List<ProductPriceSummary> priceSummaries = productReader.readAllPriceSummaries();

        CategoryPriceAggregation aggregation = priceAggregator.aggregatePricesOfCategory(priceSummaries);

        List<ProductPrice> productPrices = aggregation.allPricesOf(PriceType.MINIMUM_PRICE);

        int totalPrice = aggregation.calculateTotalPrice(PriceType.MINIMUM_PRICE);

        Brands brands = brandReader.readAllByIds(productPrices.stream()
                .map(ProductPrice::brandId)
                .collect(Collectors.toSet()));

        List<CategoryBrandPrice> categoryBrandPrices = productPrices.stream()
                .map(productPrice -> CategoryBrandPrice.builder()
                        .category(productPrice.category())
                        .brandName(brands.findNameById(productPrice.brandId())
                                .orElse("Unknown"))
                        .price(productPrice.price())
                        .build())
                .toList();

        return CategoryMinimumPrices.builder()
                .prices(categoryBrandPrices)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional(readOnly = true)
    public BrandMinimumTotalPriceResponse retrieveBrandMinimumTotalPrice() {
        List<ProductPriceSummary> summaries = productReader.readAllPriceSummaries();

        BrandCategoryPriceAggregation aggregation =
                priceAggregator.aggregateBrandCategoryMinimumPrices(summaries);

        long minimumPriceBrandId = aggregation.minimumTotalPriceBrandId();

        Brand brand = brandReader.readById(minimumPriceBrandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        List<ProductPrice> prices = aggregation.minimumPricesOf(minimumPriceBrandId);

        int totalPrice = aggregation.calculateMinimumTotalPriceOf(minimumPriceBrandId);

        List<CategoryPrice> categoryPrices = prices.stream()
                .map(productPrice -> CategoryPrice.builder()
                        .category(productPrice.category())
                        .price(productPrice.price())
                        .build())
                .toList();

        return BrandMinimumTotalPriceResponse.builder()
                .minimumPrice(BrandTotalPrice.builder()
                        .brandName(brand.getName())
                        .categories(categoryPrices)
                        .totalPrice(totalPrice)
                        .build())
                .build();
    }
}
