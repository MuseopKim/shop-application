package commerce.shop.domain.product;


public record ProductPriceSummary(
        Category category,
        long brandId,
        int minimumPrice,
        int maximumPrice
) {

}
