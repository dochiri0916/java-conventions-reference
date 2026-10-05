package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

import java.util.List;

public record ProductSummaries(List<ListProductsResult.ProductSummary> values) {

    public ProductSummaries {
        values = List.copyOf(values);
    }

}
