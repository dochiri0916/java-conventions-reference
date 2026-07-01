package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.GetCartResult;
import com.dochiri.tooltestlab.cart.application.port.in.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class GetCartService implements GetCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetCartResult getCart(final String memberPublicId) {
        final MemberId memberId = new MemberId(memberPublicId);
        final Cart cart = cartRepositoryPort.findByMemberId(memberPublicId)
                .orElseGet(() -> Cart.create(memberId));

        final var items = cart.items().values().stream()
                .map(i -> new GetCartResult.Item(
                        i.productId().value(), i.productName().value(), i.quantity().value(),
                        i.unitPrice().amount(), i.totalPrice().amount()))
                .toList();

        return new GetCartResult(cart.id().value(), cart.memberId().value(), items, cart.totalAmount().amount());
    }
}
