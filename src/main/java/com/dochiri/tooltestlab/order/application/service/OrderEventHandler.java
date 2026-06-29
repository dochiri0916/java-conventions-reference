package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelledEvent;
import com.dochiri.tooltestlab.order.domain.event.OrderPlacedEvent;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final ProductRepositoryPort productRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    public OrderEventHandler(final ProductRepositoryPort productRepositoryPort, final CartRepositoryPort cartRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.cartRepositoryPort = cartRepositoryPort;
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderPlaced(final OrderPlacedEvent event) {
        log.info("[OrderEventHandler] 주문 생성 이벤트 수신. orderId={}, items={}", event.orderId().value(), event.items().size());

        for (var item : event.items()) {
            final Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId().value()));
            final Product decreased = product.decreaseStock(item.quantity().value());
            productRepositoryPort.save(decreased, List.of());
            log.debug("재고 차감 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }

        cartRepositoryPort.findByMemberId(event.memberId().value()).ifPresent(cart -> {
            cartRepositoryPort.save(cart.clear());
            log.debug("장바구니 비우기 완료. memberId={}", event.memberId().value());
        });
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderCancelled(final OrderCancelledEvent event) {
        log.info("[OrderEventHandler] 주문 취소 이벤트 수신. orderId={}, items={}", event.orderId().value(), event.items().size());

        for (var item : event.items()) {
            final Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId().value()));
            final Product restored = product.increaseStock(item.quantity().value());
            productRepositoryPort.save(restored, List.of());
            log.debug("재고 복구 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }
    }
}
