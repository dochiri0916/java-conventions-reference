package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.ClearCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class ClearCartService implements ClearCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    @Override
    @Transactional
    public void clear(final String memberPublicId) {
        cartRepositoryPort.findByMemberId(memberPublicId)
                .ifPresent(cart -> cartRepositoryPort.save(cart.clear()));
    }
}
