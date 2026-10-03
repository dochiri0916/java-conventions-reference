package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import com.dochiri.tooltestlab.order.domain.model.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String orderId;

    @Column(nullable = false, length = 32)
    private String memberId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private long totalAmount;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 10)
    private String zipCode;

    private OrderEntity(
            final Long id,
            final String orderId,
            final String memberId,
            final String status,
            final long totalAmount,
            String recipientName,
            final String phone,
            final String address,
            final String zipCode
    ) {
        this.id = id;
        this.orderId = orderId;
        this.memberId = memberId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
    }

    static OrderEntity of(final Order domain) {
        return new OrderEntity(
                null, domain.id().value(), domain.memberId().value(), domain.status().name(),
                domain.totalAmount().amount(),
                domain.shippingAddress().recipientName().value(), domain.shippingAddress().phone().value(),
                domain.shippingAddress().address().value(), domain.shippingAddress().zipCode().value());
    }

    void apply(final Order domain) {
        this.status = domain.status().name();
        this.totalAmount = domain.totalAmount().amount();
        this.recipientName = domain.shippingAddress().recipientName().value();
        this.phone = domain.shippingAddress().phone().value();
        this.address = domain.shippingAddress().address().value();
        this.zipCode = domain.shippingAddress().zipCode().value();
    }
}
