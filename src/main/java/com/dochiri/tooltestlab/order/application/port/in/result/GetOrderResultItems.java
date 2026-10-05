package com.dochiri.tooltestlab.order.application.port.in.result;

import java.util.List;

public record GetOrderResultItems(List<GetOrderResult.Item> values) {

    public GetOrderResultItems {
        values = List.copyOf(values);
    }

}
