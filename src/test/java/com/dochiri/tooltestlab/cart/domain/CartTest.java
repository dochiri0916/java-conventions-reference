package com.dochiri.tooltestlab.cart.domain;

import com.dochiri.tooltestlab.cart.domain.vo.CartItems;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    @Test
    @DisplayName("장바구니 변경은 ID와 원본을 유지하고 같은 상품의 수량을 합산한다")
    void changes_cart_immutably() {
        // given
        final Cart empty = Cart.create(MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"));
        final ProductReferenceId productId = ProductReferenceId.parse("01900000-0000-7000-8000-000000000002");
        final ProductName name = ProductName.of("상품");

        // when
        final Cart added = empty.addItem(productId, name, Quantity.of(2), Money.of(100));
        final Cart increased = added.addItem(productId, name, Quantity.of(3), Money.of(100));
        final Cart removed = increased.removeItem(productId);
        final Cart cleared = increased.clear();

        // then
        assertTrue(empty.isEmpty());
        assertEquals(2, added.items().values().getFirst().quantity().value());
        assertEquals(1, increased.items().size());
        assertEquals(5, increased.items().values().getFirst().quantity().value());
        assertEquals(Money.of(500), increased.totalAmount());
        assertEquals(empty.id(), increased.id());
        assertEquals(empty.memberId(), increased.memberId());
        assertTrue(removed.isEmpty());
        assertTrue(cleared.isEmpty());
        assertEquals(increased.id(), removed.id());
        assertEquals(increased.id(), cleared.id());
    }

    @Test
    @DisplayName("장바구니 생성과 복원은 null 필수 필드를 거부한다")
    void rejects_null_fields() {
        // given
        final Cart cart = Cart.create(MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"));

        // when & then
        assertThrows(NullPointerException.class, () -> Cart.create(null));
        assertThrows(NullPointerException.class, () -> Cart.restore(null, cart.memberId(), CartItems.empty()));
        assertThrows(NullPointerException.class, () -> Cart.restore(cart.id(), null, CartItems.empty()));
        assertThrows(NullPointerException.class, () -> Cart.restore(cart.id(), cart.memberId(), null));
    }
}
