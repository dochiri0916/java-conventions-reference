package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.ProductReferenceId;
import com.dochiri.tooltestlab.cart.domain.model.MemberReferenceId;
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
        final MemberReferenceId memberId = new MemberReferenceId(command.memberPublicId());
        final ProductReferenceId productId = new ProductReferenceId(command.productPublicId());

        Cart cart = cartRepositoryPort.findByMemberId(memberId.value())
                .orElseThrow(() -> new CartNotFoundException());

        cart = cart.removeItem(productId);
        cartRepositoryPort.save(cart);
    }
}
