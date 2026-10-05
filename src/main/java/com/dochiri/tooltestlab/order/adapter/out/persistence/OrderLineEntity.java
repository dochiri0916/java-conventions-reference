package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "order_lines",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_order_lines_public_id",
                columnNames = "public_id"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, length = 36)
    private String publicId;

    @Column(nullable = false, length = 36)
    private String orderId;

    @Column(nullable = false, length = 36)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    private OrderLineEntity(final String publicId, final String orderId, final String productId,
                           final String productName, final int quantity, final long unitPrice) {
        this.publicId = publicId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    static OrderLineEntity create(final String publicId, final String orderId, final String productId,
                                  final String productName, final int quantity, final long unitPrice) {
        return new OrderLineEntity(publicId, orderId, productId, productName, quantity, unitPrice);
    }
}
