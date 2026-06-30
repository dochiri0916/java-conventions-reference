package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartItemLimitExceededException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartBranchTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = Cart.create(new MemberId("m1"));
    }

    @Test
    @DisplayName("addItem - 새 상품 추가")
    void addNewItem() {
            // given

            // when

            // then

        var updated = cart.addItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        assertThat(updated.items().size()).isEqualTo(1);
        assertThat(updated.totalAmount()).isEqualTo(Money.of(1000));
    }

    @Test
    @DisplayName("addItem - 동일 상품 추가 시 수량 증가")
    void addExistingItem() {
            // given

            // when

            // then

        var updated = cart.addItem(new ProductId("p1"), new ProductName("상품"), new Quantity(2), Money.of(1000));
        var updated2 = updated.addItem(new ProductId("p1"), new ProductName("상품"), new Quantity(3), Money.of(1000));
        assertThat(updated2.items().size()).isEqualTo(1);
        assertThat(updated2.items().values().get(0).quantity().value()).isEqualTo(5);
    }

    @Test
    @DisplayName("아이템 제거")
    void removeItem() {
            // given

            // when

            // then

        var updated = cart.addItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        var removed = updated.removeItem(new ProductId("p1"));
        assertThat(removed.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("removeItem - 없는 상품 제거 시 예외")
    void removeNonExistentItem() {
            // given

            // when

            // then

        assertThatThrownBy(() -> cart.removeItem(new ProductId("p1")))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("비우기")
    void clear() {
            // given

            // when

            // then

        var updated = cart.addItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));
        var cleared = updated.clear();
        assertThat(cleared.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
            // given

            // when

            // then

        assertThat(cart.equals("string")).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
            // given

            // when

            // then

        var c1 = Cart.create(new MemberId("m1"));
        var c2 = new Cart(c1.id(), new MemberId("m2"), CartItems.empty());
        assertThat(c1.equals(c2)).isTrue();
    }

    @Test
    @DisplayName("CartItems - 최대 개수 초과 예외")
    void maxItemsExceeded() {
            // given

            // when

            // then

        assertThatThrownBy(() -> {
            CartItems items = CartItems.empty();
            for (int i = 0; i < 101; i++) {
                items = items.add(new CartItem(new ProductId("p" + i), new ProductName("상품"),
                        new Quantity(1), Money.of(1000)));
            }
        }).isInstanceOf(CartItemLimitExceededException.class);
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
            // given

            // when
        var result = cart.equals(cart);

            // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 ID를 가지면 false")
    void equalsDifferentId() {
            // given
        var c1 = Cart.create(new MemberId("m1"));
        var c2 = Cart.create(new MemberId("m2"));

            // when
        var result = c1.equals(c2);

            // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("CartItems - null 목록이면 NullPointerException")
    void cartItemsNullValues() {
            // given

            // when

            // then
        assertThatThrownBy(() -> new CartItems(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("CartItems - null 요소가 포함되면 예외")
    void cartItemsNullElement() {
            // given
        var list = new java.util.ArrayList<CartItem>();
        list.add(new CartItem(new ProductId("p1"), new ProductName("상품"),
                new Quantity(1), Money.of(1000)));
        list.add(null);

            // when

            // then
        assertThatThrownBy(() -> CartItems.from(list))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("CartItems - null 아이템 추가 시 NullPointerException")
    void cartItemsAddNull() {
            // given
        var items = CartItems.empty();

            // when

            // then
        assertThatThrownBy(() -> items.add(null))
                .isInstanceOf(NullPointerException.class);
    }
}
