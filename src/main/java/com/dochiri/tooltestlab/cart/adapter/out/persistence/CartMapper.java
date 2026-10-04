package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.CartId;
import com.dochiri.tooltestlab.cart.domain.vo.CartItem;
import com.dochiri.tooltestlab.cart.domain.vo.CartItems;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;

import java.util.List;
import com.github.f4b6a3.uuid.UuidCreator;

final class CartMapper {

    private CartMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Cart toDomain(final CartEntity entity, final List<CartItemEntity> itemEntities) {
        final List<CartItem> domainItems = itemEntities.stream()
                .map(e -> CartItem.of(ProductReferenceId.parse(e.getProductId()),
                        ProductName.of(e.getProductName()), Quantity.of(e.getQuantity()), Money.of(e.getUnitPrice())))
                .toList();
        return Cart.restore(
                CartId.parse(entity.getCartId()),
                MemberReferenceId.parse(entity.getMemberId()),
                CartItems.of(domainItems), entity.getRevision());
    }

    static CartEntity toEntity(final Cart domain) {
        final CartEntity entity = CartEntity.create(domain.id().value(), domain.memberId().value());
        entity.updateRevision(domain.revision());
        return entity;
    }

    static List<CartItemEntity> toItemEntities(final Cart domain, final String cartId) {
        return domain.items().values().stream()
                .map(i -> CartItemEntity.create(
                        UuidCreator.getTimeOrderedEpoch().toString(),
                        cartId,
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
    }
}
