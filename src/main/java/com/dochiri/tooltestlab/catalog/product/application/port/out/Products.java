package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.domain.Product;

import java.util.List;

public record Products(List<Product> values) {

    public Products {
        values = List.copyOf(values);
    }

}
