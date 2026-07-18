package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.port.in.HandleOrderPlacedUseCase;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class HandleOrderPlacedService implements HandleOrderPlacedUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(HandleOrderPlacedService.class);

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional
    public void handleOrderPlaced(final OrderPlaced event) {
        LOG.info(
                "[HandleOrderPlacedService] 주문 생성 이벤트 수신. orderId={}, items={}",
                event.orderId().value(),
                event.items().values().size());

        for (var item : event.items().values()) {
            final Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> ProductNotFoundException.productNotFound(item.productId().value()));
            final Product decreased = product.decreaseStock(item.quantity().value());
            productRepositoryPort.save(decreased, List.of());
            LOG.debug("재고 차감 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }
    }
}
