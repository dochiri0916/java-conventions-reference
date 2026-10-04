package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
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

    @Getter(AccessLevel.NONE)
    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 36)
    private String orderId;

    @Column(nullable = false, length = 36)
    private String memberId;

    @Column(nullable = false, length = 36, updatable = false)
    private String cartId;

    @Column(nullable = false, updatable = false)
    private long cartRevision;

    void setCheckout(final String cartId, final long cartRevision) {
        this.cartId = cartId;
        this.cartRevision = cartRevision;
    }

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
            final String orderId,
            final String memberId,
            final String status,
            final long totalAmount,
            final String recipientName,
            final String phone,
            final String address,
            final String zipCode
    ) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
    }

    static OrderEntity create(
            final String orderId,
            final String memberId,
            final String status,
            final long totalAmount,
            final String recipientName,
            final String phone,
            final String address,
            final String zipCode
    ) {
        return new OrderEntity(
                orderId, memberId, status, totalAmount, recipientName, phone, address, zipCode);
    }

    void update(
            final String status,
            final long totalAmount,
            final String recipientName,
            final String phone,
            final String address,
            final String zipCode
    ) {
        this.status = status;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
    }
}
