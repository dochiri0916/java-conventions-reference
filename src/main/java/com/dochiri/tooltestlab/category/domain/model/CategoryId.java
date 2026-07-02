package com.dochiri.tooltestlab.category.domain.model;

import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException;

import java.util.UUID;

public record CategoryId(String value) {

    public CategoryId {
        if (value == null) {
            throw InvalidCategoryIdException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidCategoryIdException.invalidCatId();
        }
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString().replace("-", ""));
    }
}
