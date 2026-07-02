package com.dochiri.tooltestlab.category.domain.model;

import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException;

public record CategoryName(String value) {

    public CategoryName {
        if (value == null) {
            throw InvalidCategoryNameException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidCategoryNameException.invalidCatName();
        }
    }
}
