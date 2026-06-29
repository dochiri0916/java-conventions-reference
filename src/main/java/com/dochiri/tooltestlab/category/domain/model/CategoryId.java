package com.dochiri.tooltestlab.category.domain.model;

import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record CategoryId(String value) {

    public CategoryId {
        value = requireNonNull(value).strip();
        if (value.isBlank()) {
            throw new InvalidCategoryIdException();
        }
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString().replace("-", ""));
    }
}
