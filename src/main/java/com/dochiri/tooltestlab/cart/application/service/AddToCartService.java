package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort;
import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort.ProductSnapshot;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.MemberReferenceId;
import com.dochiri.tooltestlab.cart.domain.model.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.Quantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class AddToCartService implements AddToCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductCatalogPort productCatalogPort;

    @Override
    @Transactional
    public void add(final AddToCartCommand command) {
        final MemberReferenceId memberId = new MemberReferenceId(command.memberPublicId());
        final ProductSnapshot product = productCatalogPort.findProduct(
                new ProductReferenceId(command.productPublicId()));

        Cart cart = cartRepositoryPort.findByMemberId(memberId.value())
                .orElseGet(() -> Cart.create(memberId));

        cart = cart.addItem(product.id(), product.name(), new Quantity(command.quantity()), product.price());
        cartRepositoryPort.save(cart);
    }
}
