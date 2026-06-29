package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    @DisplayName("장바구니를 생성할 수 있다")
    void createCart() {
        final Cart cart = Cart.create(new MemberId("member1"));
        assertThat(cart.id().value()).isNotBlank();
        assertThat(cart.memberId().value()).isEqualTo("member1");
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니에 상품을 추가할 수 있다")
    void addItem() {
        final Cart cart = Cart.create(new MemberId("member1"));
        final Cart updated = cart.addItem(new ProductId("product1"), new ProductName("노트북"), 2, Money.of(1_000_000L));

        assertThat(updated.items().values()).hasSize(1);
        assertThat(updated.totalAmount()).isEqualTo(Money.of(2_000_000L));
    }

    @Test
    @DisplayName("동일 상품 추가 시 수량이 증가한다")
    void addSameProductIncreasesQuantity() {
        final Cart cart = Cart.create(new MemberId("member1"));
        final Cart updated = cart.addItem(new ProductId("product1"), new ProductName("노트북"), 2, Money.of(1_000_000L));
        final Cart updated2 = updated.addItem(new ProductId("product1"), new ProductName("노트북"), 3, Money.of(1_000_000L));

        assertThat(updated2.items().values()).hasSize(1);
        assertThat(updated2.items().values().get(0).quantity()).isEqualTo(5);
        assertThat(updated2.totalAmount()).isEqualTo(Money.of(5_000_000L));
    }

    @Test
    @DisplayName("장바구니에서 상품을 제거할 수 있다")
    void removeItem() {
        final Cart cart = Cart.create(new MemberId("member1"));
        final Cart updated = cart.addItem(new ProductId("product1"), new ProductName("노트북"), 2, Money.of(1_000_000L));
        final Cart removed = updated.removeItem(new ProductId("product1"));

        assertThat(removed.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니를 비울 수 있다")
    void clear() {
        final Cart cart = Cart.create(new MemberId("member1"));
        final Cart updated = cart.addItem(new ProductId("product1"), new ProductName("노트북"), 2, Money.of(1_000_000L));
        final Cart cleared = updated.clear();

        assertThat(cleared.isEmpty()).isTrue();
        assertThat(cleared.totalAmount()).isEqualTo(Money.ZERO);
    }
}
