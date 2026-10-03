package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String cartItemId;

    @Column(nullable = false, length = 36)
    private String cartId;

    @Column(nullable = false, length = 36)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    private CartItemEntity(final String cartItemId, final String cartId, final String productId,
                          final String productName, final int quantity, final long unitPrice) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    static CartItemEntity create(final String cartItemId, final String cartId, final String productId,
                                 final String productName, final int quantity, final long unitPrice) {
        return new CartItemEntity(cartItemId, cartId, productId, productName, quantity, unitPrice);
    }
}
