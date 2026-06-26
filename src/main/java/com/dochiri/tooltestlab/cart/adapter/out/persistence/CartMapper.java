package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;

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
                .map(e -> new CartItem(e.getProductPublicId(), e.getProductName(), e.getQuantity(), e.getUnitPrice()))
                .toList();
        return new Cart(entity.getPublicId(), entity.getMemberPublicId(), domainItems);
    }

    static CartEntity toEntity(Cart domain) {
        return new CartEntity(null, domain.publicId(), domain.memberPublicId());
    }

    static List<CartItemEntity> toItemEntities(Cart domain, Long cartId) {
        return domain.items().stream()
                .map(i -> new CartItemEntity(null, cartId, i.productPublicId(), i.productName(), i.quantity(), i.unitPrice()))
                .toList();
    }
}
