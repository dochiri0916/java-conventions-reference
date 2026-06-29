package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.command.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.command.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.port.in.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.application.result.GetCartResult;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.Money;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService implements GetCartUseCase, AddToCartUseCase, RemoveFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public CartService(CartRepositoryPort cartRepositoryPort, ProductRepositoryPort productRepositoryPort) {
        this.cartRepositoryPort = cartRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public GetCartResult getCart(String memberPublicId) {
        MemberId memberId = new MemberId(memberPublicId);
        Cart cart = cartRepositoryPort.findByMemberId(memberPublicId)
                .orElseGet(() -> Cart.create(memberId));

        var items = cart.items().values().stream()
                .map(i -> new GetCartResult.Item(
                        i.productId().value(), i.productName(), i.quantity(),
                        i.unitPrice().amount(), i.totalPrice().amount()))
                .toList();

        return new GetCartResult(cart.id().value(), cart.memberId().value(), items, cart.totalAmount().amount());
    }

    @Override
    public void add(AddToCartCommand command) {
        MemberId memberId = new MemberId(command.memberPublicId());

        // 트랜잭션 내에서 상품 존재 여부와 최신 가격을 조회한다.
        Product product = productRepositoryPort.findByPublicId(command.productPublicId())
                .orElseThrow(() -> new ProductNotFoundException(command.productPublicId()));

        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseGet(() -> Cart.create(memberId));

        cart = cart.addItem(product.id(), product.name().value(),
                command.quantity(), Money.of(product.price().amount()));
        cartRepositoryPort.save(cart);
    }

    @Override
    public void remove(RemoveFromCartCommand command) {
        Cart cart = cartRepositoryPort.findByMemberId(command.memberPublicId())
                .orElseThrow(() -> new CartNotFoundException(command.memberPublicId()));

        cart = cart.removeItem(new ProductId(command.productPublicId()));
        cartRepositoryPort.save(cart);
    }
}
