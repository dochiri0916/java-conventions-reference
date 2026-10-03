package com.dochiri.tooltestlab.cart.domain.vo;

import com.dochiri.tooltestlab.cart.domain.exception.CartItemRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.exception.TooManyCartItemsException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartItemsTest {

    private static CartItem item(final String id, final int quantity) {
        return CartItem.of(ProductReferenceId.parse(id), ProductName.of("상품"), Quantity.of(quantity), Money.of(100));
    }

    @Test
    @DisplayName("장바구니 목록은 원본과 반환 목록의 변경으로부터 보호된다")
    void copiesAndProtectsValues() {
        // given
        final CartItem item = item("01900000-0000-7000-8000-000000000001", 2);
        final List<CartItem> source = new ArrayList<>(List.of(item));

        // when
        final CartItems items = CartItems.of(source);
        source.clear();

        // then
        assertEquals(1, items.size());
        assertEquals(List.of(item), items.values());
        assertEquals(CartItems.of(List.of(item)), items);
        assertEquals(CartItems.of(List.of(item)).hashCode(), items.hashCode());
        assertThrows(UnsupportedOperationException.class, () -> items.values().clear());
        assertEquals(Money.of(200), items.totalAmount());
    }

    @Test
    @DisplayName("장바구니 목록은 null 요소와 최대 항목 수 초과를 거부한다")
    void validatesCollectionBoundaries() {
        // given
        final CartItem item = item("01900000-0000-7000-8000-000000000001", 1);

        // when
        final CartItems maximum = CartItems.of(Collections.nCopies(100, item));

        // then
        assertEquals(100, maximum.size());
        assertThrows(CartItemRequiredException.class, () -> CartItems.of(null));
        assertThrows(InvalidCartItemException.class, () -> CartItems.of(Arrays.asList(item, null)));
        assertThrows(TooManyCartItemsException.class, () -> CartItems.of(Collections.nCopies(101, item)));
        assertTrue(CartItems.empty().isEmpty());
        assertEquals(Money.ZERO, CartItems.empty().totalAmount());
    }

    @Test
    @DisplayName("장바구니 항목 추가 교체 삭제는 원본 목록을 유지한다")
    void changesCollectionsImmutably() {
        // given
        final CartItem first = item("01900000-0000-7000-8000-000000000001", 1);
        final CartItem second = item("01900000-0000-7000-8000-000000000002", 2);
        final CartItems original = CartItems.of(List.of(first));

        // when
        final CartItems added = original.add(second);
        final CartItem changed = first.increaseQuantity(2);
        final CartItems replaced = added.replace(first, changed);
        final CartItems removed = replaced.remove(second.productId());

        // then
        assertEquals(List.of(first), original.values());
        assertEquals(List.of(first, second), added.values());
        assertEquals(List.of(changed, second), replaced.values());
        assertEquals(List.of(changed), removed.values());
        assertTrue(added.containsProduct(second.productId()));
        assertEquals(second, added.findByProductId(second.productId()).orElseThrow());
        assertEquals(Money.of(300), removed.totalAmount());
        assertThrows(InvalidCartItemException.class, () -> original.remove(second.productId()));
        assertThrows(InvalidCartItemException.class, () -> original.replace(second, first));
    }
}
