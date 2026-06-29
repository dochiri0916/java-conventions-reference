package com.dochiri.tooltestlab.order.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_lines")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false, length = 32)
    private String productPublicId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    public OrderLineEntity(final Long id, final Long orderId, final String productPublicId, final String productName, final int quantity, final long unitPrice) {
        this.id = id;
        this.productPublicId = productPublicId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderId = orderId;
    }
}