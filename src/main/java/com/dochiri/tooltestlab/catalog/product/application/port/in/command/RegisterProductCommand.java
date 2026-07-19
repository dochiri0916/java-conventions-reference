package com.dochiri.tooltestlab.catalog.product.application.port.in.command;

import java.util.Set;

public record RegisterProductCommand(
        String name,
        String description,
        long price,
        int stock,
        Set<String> categoryIds
) {
    public RegisterProductCommand {
        categoryIds = categoryIds == null ? Set.of() : Set.copyOf(categoryIds);
    }
}
