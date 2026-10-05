package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;

import java.util.Set;

public record CategoryNameSet(Set<CategoryName> values) {

    public CategoryNameSet {
        values = Set.copyOf(values);
    }

}
