package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.port.in.command.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.cart.application.CartEventMapper;
import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class RemoveFromCartService implements RemoveFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void remove(final RemoveFromCartCommand command) {
        final MemberReferenceId memberId = MemberReferenceId.parse(command.memberPublicId());
        final ProductReferenceId productId = ProductReferenceId.parse(command.productPublicId());

        Cart cart = cartRepositoryPort.findByMemberIdForUpdate(memberId)
                .orElseThrow(() -> new CartNotFoundException());

        cart = cart.removeItem(productId);
        final Cart saved = cartRepositoryPort.save(cart);
        integrationEventPort.publish(CartEventMapper.toIntegration(saved.changedEvent()));
    }
}
