package commerce.shop.domain.product;


import commerce.shop.domain.category.Category;

public record ProductPriceSummary(
        Category category,
        long brandId,
        int minimumPrice,
        int maximumPrice
) {

}
