package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    @DisplayName("장바구니를 생성할 수 있다")
    void createCart() {
        // given
        var memberId = new MemberId("member1");

        // when
        final Cart cart = Cart.create(memberId);

        // then
        assertThat(cart.id().value()).isNotBlank();
        assertThat(cart.memberId().value()).isEqualTo("member1");
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니에 상품을 추가할 수 있다")
    void addItem() {
        // given
        final Cart cart = Cart.create(new MemberId("member1"));
        var productId = new ProductId("product1");
        var productName = new ProductName("노트북");
        var quantity = new Quantity(2);
        var unitPrice = Money.of(1_000_000L);

        // when
        final Cart updated = cart.addItem(productId, productName, quantity, unitPrice);

        // then
        assertThat(updated.items().values()).hasSize(1);
        assertThat(updated.totalAmount()).isEqualTo(Money.of(2_000_000L));
    }

    @Test
    @DisplayName("동일 상품 추가 시 수량이 증가한다")
    void addSameProductIncreasesQuantity() {
        // given
        final Cart cart = Cart.create(new MemberId("member1"));
        var productId = new ProductId("product1");
        var productName = new ProductName("노트북");
        var unitPrice = Money.of(1_000_000L);

        // when
        final Cart updated = cart.addItem(productId, productName, new Quantity(2), unitPrice);
        final Cart updated2 = updated.addItem(productId, productName, new Quantity(3), unitPrice);

        // then
        assertThat(updated2.items().values()).hasSize(1);
        assertThat(updated2.items().values().get(0).quantity()).isEqualTo(new Quantity(5));
        assertThat(updated2.totalAmount()).isEqualTo(Money.of(5_000_000L));
    }

    @Test
    @DisplayName("장바구니에서 상품을 제거할 수 있다")
    void removeItem() {
        // given
        final Cart cart = Cart.create(new MemberId("member1"));
        var productId = new ProductId("product1");
        var productName = new ProductName("노트북");
        var quantity = new Quantity(2);
        var unitPrice = Money.of(1_000_000L);
        final Cart updated = cart.addItem(productId, productName, quantity, unitPrice);

        // when
        final Cart removed = updated.removeItem(productId);

        // then
        assertThat(removed.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니를 비울 수 있다")
    void clear() {
        // given
        final Cart cart = Cart.create(new MemberId("member1"));
        var productId = new ProductId("product1");
        var productName = new ProductName("노트북");
        var quantity = new Quantity(2);
        var unitPrice = Money.of(1_000_000L);
        final Cart updated = cart.addItem(productId, productName, quantity, unitPrice);

        // when
        final Cart cleared = updated.clear();

        // then
        assertThat(cleared.isEmpty()).isTrue();
        assertThat(cleared.totalAmount()).isEqualTo(Money.ZERO);
    }
}
