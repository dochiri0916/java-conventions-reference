package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.in.HandleOrderPlacedUseCase;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class ClearCartOnOrderPlacedService implements HandleOrderPlacedUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ClearCartOnOrderPlacedService.class);

    private final CartRepositoryPort cartRepositoryPort;

    @Override
    @Transactional
    public void handleOrderPlaced(final OrderPlaced event) {
        cartRepositoryPort.findByMemberId(event.memberId().value())
                .map(cart -> cartRepositoryPort.save(cart.clear()))
                .ifPresent(cart -> LOG.debug("장바구니 비우기 완료. memberId={}", cart.memberId().value()));
    }
}
