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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cartId;

    @Column(nullable = false, length = 32)
    private String productPublicId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    public CartItemEntity(final Long id, final Long cartId, final String productPublicId,
                          String productName, final int quantity, final long unitPrice) {
        this.id = id;
        this.cartId = cartId;
        this.productPublicId = productPublicId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
