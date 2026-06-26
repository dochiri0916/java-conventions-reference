package com.dochiri.tooltestlab.cart.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    @DisplayName("장바구니를 생성할 수 있다")
    void createCart() {
        Cart cart = Cart.create("member1");
        assertThat(cart.publicId()).isNotBlank();
        assertThat(cart.memberPublicId()).isEqualTo("member1");
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니에 상품을 추가할 수 있다")
    void addItem() {
        Cart cart = Cart.create("member1");
        Cart updated = cart.addItem("product1", "노트북", 2, 1_000_000L);

        assertThat(updated.items()).hasSize(1);
        assertThat(updated.totalAmount()).isEqualTo(2_000_000L);
    }

    @Test
    @DisplayName("동일 상품 추가 시 수량이 증가한다")
    void addSameProductIncreasesQuantity() {
        Cart cart = Cart.create("member1");
        Cart updated = cart.addItem("product1", "노트북", 2, 1_000_000L);
        Cart updated2 = updated.addItem("product1", "노트북", 3, 1_000_000L);

        assertThat(updated2.items()).hasSize(1);
        assertThat(updated2.items().get(0).quantity()).isEqualTo(5);
        assertThat(updated2.totalAmount()).isEqualTo(5_000_000L);
    }

    @Test
    @DisplayName("장바구니에서 상품을 제거할 수 있다")
    void removeItem() {
        Cart cart = Cart.create("member1");
        Cart updated = cart.addItem("product1", "노트북", 2, 1_000_000L);
        Cart removed = updated.removeItem("product1");

        assertThat(removed.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("장바구니를 비울 수 있다")
    void clear() {
        Cart cart = Cart.create("member1");
        Cart updated = cart.addItem("product1", "노트북", 2, 1_000_000L);
        Cart cleared = updated.clear();

        assertThat(cleared.isEmpty()).isTrue();
        assertThat(cleared.totalAmount()).isEqualTo(0L);
    }
}
