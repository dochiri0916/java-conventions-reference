package com.dochiri.tooltestlab.catalog.category.domain.model;

import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryNameRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryNameException;


public record CategoryName(String value) {

    public CategoryName {
        if (value == null) {
            throw new CategoryNameRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidCategoryNameException();
        }
    }
}
