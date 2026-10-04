package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.ClearCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.cart.application.CartEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class ClearCartService implements ClearCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void clear(final String memberPublicId) {
        cartRepositoryPort.findByMemberIdForUpdate(memberPublicId).ifPresent(cart -> {
            final var saved = cartRepositoryPort.save(cart.clear());
            integrationEventPort.publish(CartEventMapper.toIntegration(saved.changedEvent()));
        });
    }
}
