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

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String orderLineId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false, length = 32)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    private OrderLineEntity(final Long id, final String orderLineId, final String orderId, final String productId,
                           final String productName, final int quantity, final long unitPrice) {
        this.id = id;
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    static OrderLineEntity create(final String orderLineId, final String orderId, final String productId,
                                  final String productName, final int quantity, final long unitPrice) {
        return new OrderLineEntity(null, orderLineId, orderId, productId, productName, quantity, unitPrice);
    }
}
