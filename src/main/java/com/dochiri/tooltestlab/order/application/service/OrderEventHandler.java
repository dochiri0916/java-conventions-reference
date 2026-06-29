package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelledEvent;
import com.dochiri.tooltestlab.order.domain.event.OrderPlacedEvent;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 주문 도메인 이벤트 핸들러.
 * 주문 생성/취소의 부수 효과(재고, 장바구니)를 별도 트랜잭션으로 처리하여
 * 하나의 트랜잭션에서 여러 Aggregate 수정을 방지한다.
 */
@Component
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final ProductRepositoryPort productRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    public OrderEventHandler(ProductRepositoryPort productRepositoryPort, CartRepositoryPort cartRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.cartRepositoryPort = cartRepositoryPort;
    }

    /**
     * 주문 생성 후 — 재고 차감 & 장바구니 비우기 (별도 트랜잭션).
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("[OrderEventHandler] 주문 생성 이벤트 수신. orderId={}, items={}", event.orderId().value(), event.items().size());

        for (var item : event.items()) {
            Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> new IllegalStateException(
                            "상품을 찾을 수 없습니다. productPublicId=" + item.productId().value()));
            Product decreased = product.decreaseStock(item.quantity().value());
            productRepositoryPort.save(decreased, List.of());
            log.debug("재고 차감 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }

        // 장바구니 비우기
        cartRepositoryPort.findByMemberId(event.memberId().value()).ifPresent(cart -> {
            cartRepositoryPort.save(cart.clear());
            log.debug("장바구니 비우기 완료. memberId={}", event.memberId().value());
        });
    }

    /**
     * 주문 취소 후 — 재고 복구 (별도 트랜잭션).
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderCancelled(OrderCancelledEvent event) {
        log.info("[OrderEventHandler] 주문 취소 이벤트 수신. orderId={}, items={}", event.orderId().value(), event.items().size());

        for (var item : event.items()) {
            Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> new IllegalStateException(
                            "상품을 찾을 수 없습니다. productPublicId=" + item.productId().value()));
            Product restored = product.increaseStock(item.quantity().value());
            productRepositoryPort.save(restored, List.of());
            log.debug("재고 복구 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }
    }
}
