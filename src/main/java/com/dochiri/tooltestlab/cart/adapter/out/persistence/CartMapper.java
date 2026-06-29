package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;

import java.util.List;

final class CartMapper {

    private CartMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Cart toDomain(final CartEntity entity, final List<CartItemEntity> itemEntities) {
        final List<CartItem> domainItems = itemEntities.stream()
                .map(e -> new CartItem(new ProductId(e.getProductPublicId()),
                        new ProductName(e.getProductName()), e.getQuantity(), Money.of(e.getUnitPrice())))
                .toList();
        return new Cart(
                new CartId(entity.getPublicId()),
                new MemberId(entity.getMemberPublicId()),
                CartItems.from(domainItems));
    }

    static CartEntity toEntity(final Cart domain) {
        return new CartEntity(null, domain.id().value(), domain.memberId().value());
    }

    static List<CartItemEntity> toItemEntities(final Cart domain, final Long cartId) {
        return domain.items().values().stream()
                .map(i -> new CartItemEntity(null, cartId, i.productId().value(), i.productName().value(),
                        i.quantity(), i.unitPrice().amount()))
                .toList();
    }
}
