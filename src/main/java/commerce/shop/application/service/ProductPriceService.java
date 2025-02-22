package commerce.shop.application.service;

import commerce.shop.api.controller.model.BrandMinimumTotalPricePayload;
import commerce.shop.api.controller.model.CategoryMinimumPricesPayload;
import commerce.shop.api.controller.model.CategoryPriceRangePayload;
import commerce.shop.application.component.aggregation.ProductPriceAggregator;
import commerce.shop.application.component.aggregation.model.BrandCategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.CategoryPriceAggregation;
import commerce.shop.application.component.aggregation.model.ProductPrice;
import commerce.shop.application.component.cache.CacheKey;
import commerce.shop.application.component.cache.CacheStorage;
import commerce.shop.application.service.model.BrandTotalPrice;
import commerce.shop.application.service.model.PriceWithBrand;
import commerce.shop.application.service.model.PriceWithCategory;
import commerce.shop.application.service.model.PriceWithCategoryAndBrand;
import commerce.shop.domain.brand.Brand;
import commerce.shop.domain.brand.BrandReader;
import commerce.shop.domain.brand.Brands;
import commerce.shop.domain.category.Category;
import commerce.shop.domain.product.PriceType;
import commerce.shop.domain.product.ProductPriceSummary;
import commerce.shop.domain.product.ProductReader;
import commerce.shop.global.exception.ApiExceptionCode;
import commerce.shop.global.exception.BrandException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductPriceService {

    private final ProductReader productReader;
    private final BrandReader brandReader;
    private final ProductPriceAggregator priceAggregator;
    private final CacheStorage cacheStorage;

    @Transactional(readOnly = true)
    public CategoryMinimumPricesPayload retrieveCategoryMinimumPrices() {
        return cacheStorage.getOrElse(
                CacheKey.CATEGORY_PRICE_MINIMUM,
                CategoryMinimumPricesPayload.class,
                this::fetchCategoryMinimumPrices);
    }

    @Transactional(readOnly = true)
    public CategoryMinimumPricesPayload fetchCategoryMinimumPrices() {
        List<ProductPriceSummary> priceSummaries = productReader.readAllPriceSummaries();

        CategoryPriceAggregation aggregation = priceAggregator.aggregatePricesOfCategory(priceSummaries);

        List<ProductPrice> productPrices = aggregation.allPricesOf(PriceType.MINIMUM_PRICE);

        int totalPrice = aggregation.calculateTotalPrice(PriceType.MINIMUM_PRICE);

        Brands brands = brandReader.readAllByIds(productPrices.stream()
                .map(ProductPrice::brandId)
                .collect(Collectors.toSet()));

        List<PriceWithCategoryAndBrand> categoryBrandPrices = productPrices.stream()
                .map(productPrice -> PriceWithCategoryAndBrand.builder()
                        .category(productPrice.category())
                        .brandName(brands.findNameById(productPrice.brandId())
                                .orElse("Unknown"))
                        .price(productPrice.price())
                        .build())
                .toList();

        return CategoryMinimumPricesPayload.builder()
                .prices(categoryBrandPrices)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional(readOnly = true)
    public BrandMinimumTotalPricePayload retrieveBrandMinimumTotalPrice() {
        return cacheStorage.getOrElse(
                CacheKey.BRAND_PRICE_MINIMUM_TOTAL,
                BrandMinimumTotalPricePayload.class,
                this::fetchBrandMinimumTotalPrice);
    }

    @Transactional(readOnly = true)
    public BrandMinimumTotalPricePayload fetchBrandMinimumTotalPrice() {
        List<ProductPriceSummary> summaries = productReader.readAllPriceSummaries();

        BrandCategoryPriceAggregation aggregation =
                priceAggregator.aggregateBrandCategoryMinimumPrices(summaries);

        long minimumPriceBrandId = aggregation.minimumTotalPriceBrandId();

        Brand brand = brandReader.readById(minimumPriceBrandId)
                .orElseThrow(() -> new BrandException(ApiExceptionCode.BRAND_NOT_EXIST));

        List<ProductPrice> prices = aggregation.minimumPricesOf(minimumPriceBrandId);

        int totalPrice = aggregation.calculateMinimumTotalPriceOf(minimumPriceBrandId);

        List<PriceWithCategory> categoryPrices = prices.stream()
                .map(productPrice -> PriceWithCategory.builder()
                        .category(productPrice.category())
                        .price(productPrice.price())
                        .build())
                .toList();

        return BrandMinimumTotalPricePayload.builder()
                .minimumPrice(BrandTotalPrice.builder()
                        .brandName(brand.getName())
                        .categories(categoryPrices)
                        .totalPrice(totalPrice)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public CategoryPriceRangePayload retrieveCategoryPriceRanges(Category category) {
        String cacheKey = String.format(CacheKey.CATEGORY_PRICE_RANGE, category.name());

        return cacheStorage.getOrElse(
                cacheKey,
                CategoryPriceRangePayload.class,
                () -> fetchCategoryPriceRanges(category));
    }

    @Transactional(readOnly = true)
    public CategoryPriceRangePayload fetchCategoryPriceRanges(Category category) {
        List<ProductPriceSummary> summaries = productReader.readPriceSummaries(category);
        if (summaries.isEmpty()) {
            return CategoryPriceRangePayload.EMPTY;
        }

        CategoryPriceAggregation aggregation = priceAggregator.aggregatePricesOfCategory(summaries);

        ProductPrice minimumPrice = aggregation.priceOf(category, PriceType.MINIMUM_PRICE);
        ProductPrice maximumPrice = aggregation.priceOf(category, PriceType.MAXIMUM_PRICE);

        Brands brands = brandReader.readAllByIds(Set.of(minimumPrice.brandId(), maximumPrice.brandId()));

        return CategoryPriceRangePayload.builder()
                .category(category)
                .minimumPrices(List.of(PriceWithBrand.builder()
                        .brandName(brands.findNameById(minimumPrice.brandId())
                                .orElse("Unknown"))
                        .price(minimumPrice.price())
                        .build()))
                .maximumPrices(List.of(PriceWithBrand.builder()
                        .brandName(brands.findNameById(maximumPrice.brandId())
                                .orElse("Unknown"))
                        .price(maximumPrice.price())
                        .build()))
                .build();
    }
}
