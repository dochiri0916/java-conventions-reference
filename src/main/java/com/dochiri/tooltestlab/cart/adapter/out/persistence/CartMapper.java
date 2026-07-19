package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

import java.util.List;
import java.util.UUID;

final class CartMapper {

    private CartMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Cart toDomain(final CartEntity entity, final List<CartItemEntity> itemEntities) {
        final List<CartItem> domainItems = itemEntities.stream()
                .map(e -> new CartItem(new ProductId(e.getProductId()),
                        new ProductName(e.getProductName()), new Quantity(e.getQuantity()), new Money(e.getUnitPrice())))
                .toList();
        return new Cart(
                new CartId(entity.getCartId()),
                new MemberId(entity.getMemberId()),
                CartItems.from(domainItems));
    }

    static CartEntity toEntity(final Cart domain) {
        return CartEntity.create(domain.id().value(), domain.memberId().value());
    }

    static List<CartItemEntity> toItemEntities(final Cart domain, final String cartId) {
        return domain.items().values().stream()
                .map(i -> CartItemEntity.create(
                        UUID.randomUUID().toString().replace("-", ""),
                        cartId,
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
    }
}
