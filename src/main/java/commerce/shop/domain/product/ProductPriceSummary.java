package commerce.shop.domain.product;


import commerce.shop.domain.category.Category;

/**
 * DB에서 조회한 카테고리와 브랜드별 최저/최고 가격 정보를 표현합니다.
 *
 * {@link commerce.shop.domain.product.ProductRepository#findAllProductPricesGroupByBrandAndCategory}
 * 쿼리 결과를 매핑합니다.
 *
 * @param category 상품 카테고리
 * @param brandId 브랜드 ID
 * @param minimumPrice 해당 카테고리 / 브랜드 최저 가격
 * @param maximumPrice 해당 카테고리 / 브랜드 최고 가격
 */
public record ProductPriceSummary(
        Category category,
        long brandId,
        int minimumPrice,
        int maximumPrice
) {

}
