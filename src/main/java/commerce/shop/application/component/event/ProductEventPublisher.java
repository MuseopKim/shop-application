package commerce.shop.application.component.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvictEvent(ProductEvent event) {
        eventPublisher.publishEvent(event);
    }
}
