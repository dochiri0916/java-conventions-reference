package com.dochiri.tooltestlab.catalog.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.domain.StockReservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "catalog_stock_reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockReservationEntity {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String orderReferenceId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockReservation.Status status;
    @Lob
    @Column(nullable = false)
    private String lines;
    private String rejectionReason;

    StockReservationEntity(final String orderReferenceId) {
        this.orderReferenceId = orderReferenceId;
    }

    void update(final StockReservation.Status status, final String lines, final String rejectionReason) {
        this.status = status;
        this.lines = lines;
        this.rejectionReason = rejectionReason;
    }
}
