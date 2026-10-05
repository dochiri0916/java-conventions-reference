package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;

import java.util.Set;

public record CategoryIdSet(Set<CategoryId> values) {

    public CategoryIdSet {
        values = Set.copyOf(values);
    }

}
