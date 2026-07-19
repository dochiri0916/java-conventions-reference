package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class AddToCartService implements AddToCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional
    public void add(final AddToCartCommand command) {
        final MemberId memberId = new MemberId(command.memberPublicId());
        final ProductId productId = new ProductId(command.productPublicId());

        final Product product = productRepositoryPort.findById(productId.value())
                .orElseThrow(() -> ProductNotFoundException.productNotFound(productId.value()));

        Cart cart = cartRepositoryPort.findByMemberId(memberId.value())
                .orElseGet(() -> Cart.create(memberId));

        cart = cart.addItem(product.id(), product.name(),
                new Quantity(command.quantity()), new Money(product.price().amount()));
        cartRepositoryPort.save(cart);
    }
}
