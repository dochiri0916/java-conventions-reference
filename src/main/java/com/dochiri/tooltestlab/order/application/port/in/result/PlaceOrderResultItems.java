package com.dochiri.tooltestlab.order.application.port.in.result;

import java.util.List;

public record PlaceOrderResultItems(List<PlaceOrderResult.OrderItemResult> values) {

    public PlaceOrderResultItems {
        values = List.copyOf(values);
    }

}
