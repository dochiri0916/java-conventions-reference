package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.command.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.command.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.application.result.GetCartResult;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService implements GetCartUseCase, AddToCartUseCase, RemoveFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    public CartService(CartRepositoryPort cartRepositoryPort) {
        this.cartRepositoryPort = cartRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public GetCartResult getCart(String memberPublicId) {
        Cart cart = cartRepositoryPort.findByMemberId(memberPublicId)
                .orElseGet(() -> Cart.create(memberPublicId));

        var items = cart.items().stream()
                .map(i -> new GetCartResult.Item(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.totalPrice()))
                .toList();

        return new GetCartResult(cart.publicId(), cart.memberPublicId(), items, cart.totalAmount());
    }

    @Override
    public void add(AddToCartCommand command) {
        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseGet(() -> Cart.create(command.memberPublicId()));

        cart = cart.addItem(command.productPublicId(), command.productName(), command.quantity(), command.unitPrice());
        cartRepositoryPort.save(cart);
    }

    @Override
    public void remove(RemoveFromCartCommand command) {
        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        cart = cart.removeItem(command.productPublicId());
        cartRepositoryPort.save(cart);
    }
}
