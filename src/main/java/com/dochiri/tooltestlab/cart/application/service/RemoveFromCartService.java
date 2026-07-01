package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class RemoveFromCartService implements RemoveFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    @Override
    @Transactional
    public void remove(final RemoveFromCartCommand command) {
        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseThrow(() -> CartNotFoundException.cartNotFound(command.memberPublicId()));

        cart = cart.removeItem(new ProductId(command.productPublicId()));
        cartRepositoryPort.save(cart);
    }
}
