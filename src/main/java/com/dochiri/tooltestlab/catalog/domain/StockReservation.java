package com.dochiri.tooltestlab.catalog.domain;

import com.dochiri.tooltestlab.catalog.domain.vo.OrderReferenceId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import java.util.List;
import java.util.Objects;

public record StockReservation(OrderReferenceId orderId, Status status, List<Line> lines, String rejectionReason) {
    public StockReservation {
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(status);
        lines = List.copyOf(lines);
    }

    public StockReservation release() {
        return new StockReservation(orderId, Status.RELEASED, lines, rejectionReason);
    }

    public enum Status {
        RESERVED, REJECTED, RELEASED
    }

    public record Line(ProductId productId, Quantity quantity) {
        public Line {
            Objects.requireNonNull(productId);
            Objects.requireNonNull(quantity);
        }
    }
}
