package com.dochiri.tooltestlab.order.adapter.out.persistence.projection;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_cart_snapshots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartSnapshotEntity {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String memberReferenceId;
    @Column(nullable = false, length = 36, unique = true)
    private String cartReferenceId;
    @Column(nullable = false)
    private long revision;
    @Column(nullable = false)
    private long sequence;
    @Column(nullable = false)
    private long claimedRevision = -1;
    @Lob
    @Column(nullable = false)
    private String items;

    CartSnapshotEntity(final String memberReferenceId) {
        this.memberReferenceId = memberReferenceId;
    }

    void update(final String cartReferenceId, final long revision, final long sequence, final String items) {
        this.cartReferenceId = cartReferenceId;
        this.revision = revision;
        this.sequence = sequence;
        this.items = items;
    }

    void claim() {
        claimedRevision = revision;
    }

    void release(final long claimed) {
        if (claimedRevision == claimed) {
            claimedRevision = -1;
        }
    }
}
