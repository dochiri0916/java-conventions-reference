package com.dochiri.tooltestlab.catalog.product.application.port.in.command;

public record RegisterProductCommand(
        String name,
        String description,
        long price,
        int stock,
        CategoryPublicIds categoryIds
) {
}
