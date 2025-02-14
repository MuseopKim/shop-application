package commerce.shop.domain.product;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import commerce.shop.domain.category.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductReaderTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductReader productReader;

    @DisplayName("전체 상품 가격 요약 정보 조회")
    @Test
    void readAllPriceSummariesTest() {
        // given
        List<ProductPriceSummary> givenSummaries = List.of(
                new ProductPriceSummary(Category.TOP, 1L, 10000, 15000),
                new ProductPriceSummary(Category.OUTER, 1L, 5000, 8000),
                new ProductPriceSummary(Category.TOP, 2L, 12000, 18000),
                new ProductPriceSummary(Category.OUTER, 2L, 6000, 9000)
        );

        when(productRepository.findAllProductPricesGroupByBrandAndCategory())
                .thenReturn(givenSummaries);

        // when
        List<ProductPriceSummary> summaries = productReader.readAllPriceSummaries();

        // then
        then(summaries)
                .isNotNull()
                .hasSize(4)
                .containsExactlyElementsOf(givenSummaries);
    }
}
