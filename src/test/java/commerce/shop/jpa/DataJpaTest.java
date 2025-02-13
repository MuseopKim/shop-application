package commerce.shop.jpa;

import commerce.shop.global.jpa.JpaConfig;
import org.springframework.context.annotation.Import;

@Import(JpaConfig.class)
@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
public class DataJpaTest {

}
