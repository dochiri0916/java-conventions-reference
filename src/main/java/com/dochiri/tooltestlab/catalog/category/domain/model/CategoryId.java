package com.dochiri.tooltestlab.catalog.category.domain.model;

import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryIdRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryIdException;

import java.util.UUID;

public record CategoryId(String value) {

    public CategoryId {
        if (value == null) {
            throw new CategoryIdRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidCategoryIdException();
        }
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString().replace("-", ""));
    }
}
