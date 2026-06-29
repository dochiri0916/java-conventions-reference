package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.model.*;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;

import java.util.List;

/**
 * CartEntity ↔ Cart 변환 유틸리티.
 */
final class CartMapper {

    private CartMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Cart toDomain(CartEntity entity, List<CartItemEntity> itemEntities) {
        List<CartItem> domainItems = itemEntities.stream()
                .map(e -> new CartItem(new ProductId(e.getProductPublicId()), e.getProductName(), e.getQuantity(),
                        Money.of(e.getUnitPrice())))
                .toList();
        return new Cart(
                new CartId(entity.getPublicId()),
                new MemberId(entity.getMemberPublicId()),
                CartItems.from(domainItems));
    }

    static CartEntity toEntity(Cart domain) {
        return new CartEntity(null, domain.id().value(), domain.memberId().value());
    }

    static List<CartItemEntity> toItemEntities(Cart domain, Long cartId) {
        return domain.items().values().stream()
                .map(i -> new CartItemEntity(null, cartId, i.productId().value(), i.productName(),
                        i.quantity(), i.unitPrice().amount()))
                .toList();
    }
}
