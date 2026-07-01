package com.dochiri.tooltestlab.cart.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.dochiri.tooltestlab.cart.domain.exception.CartItemLimitExceededException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = "RV", justification = "Return value used to verify immutability or exception")
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
        var productId = new ProductId("p1");
        var productName = new ProductName("상품");
        var quantity = new Quantity(1);
        var unitPrice = Money.of(1000);

        // when
        var updated = cart.addItem(productId, productName, quantity, unitPrice);

        // then
        assertThat(updated.items().size()).isEqualTo(1);
        assertThat(updated.totalAmount()).isEqualTo(Money.of(1000));
    }

    @Test
    @DisplayName("addItem - 동일 상품 추가 시 수량 증가")
    void addExistingItem() {
        // given
        var productId = new ProductId("p1");
        var productName = new ProductName("상품");
        var unitPrice = Money.of(1000);

        // when
        var updated = cart.addItem(productId, productName, new Quantity(2), unitPrice);
        var updated2 = updated.addItem(productId, productName, new Quantity(3), unitPrice);

        // then
        assertThat(updated2.items().size()).isEqualTo(1);
        assertThat(updated2.items().values().get(0).quantity().value()).isEqualTo(5);
    }

    @Test
    @DisplayName("아이템 제거")
    void removeItem() {
        // given
        var productId = new ProductId("p1");
        var updated = cart.addItem(productId, new ProductName("상품"), new Quantity(1), Money.of(1000));

        // when
        var removed = updated.removeItem(productId);

        // then
        assertThat(removed.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("removeItem - 없는 상품 제거 시 예외")
    void removeNonExistentItem() {
        // given
        var nonExistentProductId = new ProductId("p1");

        // when & then
        assertThatThrownBy(() -> cart.removeItem(nonExistentProductId))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("비우기")
    void clear() {
        // given
        var updated = cart.addItem(new ProductId("p1"), new ProductName("상품"), new Quantity(1), Money.of(1000));

        // when
        var cleared = updated.clear();

        // then
        assertThat(cleared.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
        // given
        var other = new Object();

        // when
        var result = cart.equals(other);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
        // given
        var c1 = Cart.create(new MemberId("m1"));
        var c2 = new Cart(c1.id(), new MemberId("m2"), CartItems.empty());

        // when
        var result = c1.equals(c2);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("CartItems - 최대 개수 초과 예외")
    void maxItemsExceeded() {
        // given
        var overLimit = 101;

        // when & then
        assertThatThrownBy(() -> {
            CartItems items = CartItems.empty();
            for (int i = 0; i < overLimit; i++) {
                items = items.add(new CartItem(new ProductId("p" + i), new ProductName("상품"),
                        new Quantity(1), Money.of(1000)));
            }
        }).isInstanceOf(CartItemLimitExceededException.class);
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
        // given
        var sameInstance = cart;

        // when
        var result = cart.equals(sameInstance);

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
    @SuppressFBWarnings(value = "NP", justification = "의도적인 null 입력 검증 테스트")
    void cartItemsNullValues() {
        // given
        var nullList = (java.util.List<CartItem>) null;

        // when & then
        assertThatThrownBy(() -> new CartItems(nullList))
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

        // when & then
        assertThatThrownBy(() -> CartItems.from(list))
                .isInstanceOf(InvalidCartItemException.class);
    }

    @Test
    @DisplayName("CartItems - null 아이템 추가 시 NullPointerException")
    void cartItemsAddNull() {
        // given
        var items = CartItems.empty();

        // when & then
        assertThatThrownBy(() -> items.add(null))
                .isInstanceOf(NullPointerException.class);
    }
}
