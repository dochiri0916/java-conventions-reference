package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartMapperTest {

    @Test
    @DisplayName("장바구니와 항목 및 참조 UUID v7을 저장하고 같은 값으로 복원한다")
    void mapsCartAndItems() {
        // given
        final Cart cart = Cart.create(MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"))
                .addItem(ProductReferenceId.parse("01900000-0000-7000-8000-000000000002"),
                        ProductName.of("상품"), Quantity.of(2), Money.of(100));

        // when
        final CartEntity entity = CartMapper.toEntity(cart);
        final var itemEntities = CartMapper.toItemEntities(cart, cart.id().value());
        final Cart restored = CartMapper.toDomain(entity, itemEntities);

        // then
        assertEquals(7, restored.id().cartId().version());
        assertEquals(cart.id(), restored.id());
        assertEquals(cart.memberId(), restored.memberId());
        assertEquals(cart.items(), restored.items());
        assertEquals(Money.of(200), restored.totalAmount());
        assertEquals(36, entity.getCartId().length());
        assertEquals(36, entity.getMemberId().length());
        assertEquals(7, UUID.fromString(itemEntities.getFirst().getCartItemId()).version());
        assertEquals(cart.id().value(), itemEntities.getFirst().getCartId());
    }
}
