package com.dochiri.tooltestlab.catalog.category.application.port.in.result;

import java.util.List;

public record CategorySummaries(List<CategorySummary> values) {

    public CategorySummaries {
        values = List.copyOf(values);
    }

}
