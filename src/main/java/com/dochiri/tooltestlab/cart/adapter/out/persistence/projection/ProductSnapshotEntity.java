package com.dochiri.tooltestlab.cart.adapter.out.persistence.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_product_snapshots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSnapshotEntity {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String productReferenceId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private long priceAmount;
    @Column(nullable = false)
    private long sequence;

    ProductSnapshotEntity(final String productReferenceId) {
        this.productReferenceId = productReferenceId;
    }

    void update(final String name, final long priceAmount, final long sequence) {
        this.name = name;
        this.priceAmount = priceAmount;
        this.sequence = sequence;
    }
}
