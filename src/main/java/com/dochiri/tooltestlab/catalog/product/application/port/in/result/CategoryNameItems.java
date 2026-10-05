package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

import java.util.List;

public record CategoryNameItems(List<String> values) {

    public CategoryNameItems {
        values = List.copyOf(values);
    }

}
