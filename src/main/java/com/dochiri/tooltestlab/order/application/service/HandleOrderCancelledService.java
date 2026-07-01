package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.port.in.HandleOrderCancelledUseCase;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class HandleOrderCancelledService implements HandleOrderCancelledUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(HandleOrderCancelledService.class);

    private final ProductRepositoryPort productRepositoryPort;

    @EventListener
    @Override
    @Transactional
    public void handleOrderCancelled(final OrderCancelled event) {
        LOG.info("[HandleOrderCancelledService] 주문 취소 이벤트 수신. orderId={}, items={}", event.orderId().value(), event.items().values().size());

        for (var item : event.items().values()) {
            final Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> ProductNotFoundException.productNotFound(item.productId().value()));
            final Product restored = product.increaseStock(item.quantity().value());
            productRepositoryPort.save(restored, List.of());
            LOG.debug("재고 복구 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }
    }
}
