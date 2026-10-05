package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.Order;

import java.util.List;

public record Orders(List<Order> values) {

    public Orders {
        values = List.copyOf(values);
    }

}
