package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.port.in.*;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.shared.model.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public final class CartService implements GetCartUseCase, AddToCartUseCase, RemoveFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public CartService(final CartRepositoryPort cartRepositoryPort, final ProductRepositoryPort productRepositoryPort) {
        this.cartRepositoryPort = cartRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

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

    @Override
    public void add(final AddToCartCommand command) {
        final MemberId memberId = new MemberId(command.memberPublicId());

        final Product product = productRepositoryPort.findByPublicId(command.productPublicId())
                .orElseThrow(() -> ProductNotFoundException.productNotFound(command.productPublicId()));

        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseGet(() -> Cart.create(memberId));

        cart = cart.addItem(product.id(), product.name(),
                new Quantity(command.quantity()), Money.of(product.price().amount()));
        cartRepositoryPort.save(cart);
    }

    @Override
    public void remove(final RemoveFromCartCommand command) {
        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseThrow(() -> CartNotFoundException.cartNotFound(command.memberPublicId()));

        cart = cart.removeItem(new ProductId(command.productPublicId()));
        cartRepositoryPort.save(cart);
    }
}
