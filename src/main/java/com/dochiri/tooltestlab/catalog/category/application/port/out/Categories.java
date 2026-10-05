package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.Category;

import java.util.Set;

public record Categories(Set<Category> values) {

    public Categories {
        values = Set.copyOf(values);
    }

}
