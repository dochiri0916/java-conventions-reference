package com.dochiri.tooltestlab.cart.application.port.in.result;

import java.util.List;

public record CartResultItems(List<GetCartResult.Item> values) {

    public CartResultItems {
        values = List.copyOf(values);
    }

}
