package com.dochiri.tooltestlab.order.adapter.out.persistence;

import com.dochiri.tooltestlab.order.domain.model.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String publicId;

    @Column(nullable = false, length = 32)
    private String memberPublicId;

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

    private OrderEntity(Long id, String publicId, String memberPublicId, String status, long totalAmount,
                        String recipientName, String phone, String address, String zipCode) {
        this.id = id;
        this.publicId = publicId;
        this.memberPublicId = memberPublicId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
    }

    static OrderEntity from(Order domain) {
        return new OrderEntity(
                null, domain.id().value(), domain.memberId().value(), domain.status().name(),
                domain.totalAmount().amount(),
                domain.shippingAddress().recipientName(), domain.shippingAddress().phone(),
                domain.shippingAddress().address(), domain.shippingAddress().zipCode());
    }

    void apply(Order domain) {
        this.status = domain.status().name();
        this.totalAmount = domain.totalAmount().amount();
        this.recipientName = domain.shippingAddress().recipientName();
        this.phone = domain.shippingAddress().phone();
        this.address = domain.shippingAddress().address();
        this.zipCode = domain.shippingAddress().zipCode();
    }
}
