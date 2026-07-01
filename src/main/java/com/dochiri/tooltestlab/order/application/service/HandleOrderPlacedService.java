package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.in.HandleOrderPlacedUseCase;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
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
public final class HandleOrderPlacedService implements HandleOrderPlacedUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(HandleOrderPlacedService.class);

    private final ProductRepositoryPort productRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    @EventListener
    @Override
    @Transactional
    public void handleOrderPlaced(final OrderPlaced event) {
        LOG.info("[HandleOrderPlacedService] 주문 생성 이벤트 수신. orderId={}, items={}", event.orderId().value(), event.items().values().size());

        for (var item : event.items().values()) {
            final Product product = productRepositoryPort.findByPublicId(item.productId().value())
                    .orElseThrow(() -> ProductNotFoundException.productNotFound(item.productId().value()));
            final Product decreased = product.decreaseStock(item.quantity().value());
            productRepositoryPort.save(decreased, List.of());
            LOG.debug("재고 차감 완료. productPublicId={}, quantity={}", item.productId().value(), item.quantity());
        }

        cartRepositoryPort.findByMemberId(event.memberId().value()).ifPresent(cart -> {
            cartRepositoryPort.save(cart.clear());
            LOG.debug("장바구니 비우기 완료. memberId={}", event.memberId().value());
        });
    }
}
