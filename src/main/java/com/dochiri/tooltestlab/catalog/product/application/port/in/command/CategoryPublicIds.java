package com.dochiri.tooltestlab.catalog.product.application.port.in.command;

import java.util.Set;

public record CategoryPublicIds(Set<String> values) {

    public CategoryPublicIds {
        values = Set.copyOf(values);
    }

}
