package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.CartEventMapper;
import com.dochiri.tooltestlab.cart.application.port.in.command.CompleteCartCheckoutCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.CompleteCartCheckoutUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.*;
import com.dochiri.tooltestlab.cart.domain.vo.CartId;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class CompleteCartCheckoutService implements CompleteCartCheckoutUseCase {
    private final CartRepositoryPort cartRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void complete(final CompleteCartCheckoutCommand command) {
        final var memberId = MemberReferenceId.parse(command.memberId());
        final var cartId = CartId.parse(command.cartId());
        cartRepositoryPort.findByMemberIdForUpdate(memberId).ifPresent(cart -> {
            if (cart.id().equals(cartId) && cart.revision() == command.cartRevision()) {
                final var saved = cartRepositoryPort.save(cart.clear());
                integrationEventPort.publish(CartEventMapper.toIntegration(saved.changedEvent()));
            }
        });
    }
}
