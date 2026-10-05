package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.vo.OrderLine;

import java.util.List;

public record CartSnapshotLines(List<OrderLine> values) {

    public CartSnapshotLines {
        values = List.copyOf(values);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

}
