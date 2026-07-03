package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String cartItemId;

    @Column(nullable = false)
    private String cartId;

    @Column(nullable = false, length = 32)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    private CartItemEntity(final Long id, final String cartItemId, final String cartId, final String productId,
                          final String productName, final int quantity, final long unitPrice) {
        this.id = id;
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    static CartItemEntity create(final String cartItemId, final String cartId, final String productId,
                                 final String productName, final int quantity, final long unitPrice) {
        return new CartItemEntity(null, cartItemId, cartId, productId, productName, quantity, unitPrice);
    }
}
