package com.dochiri.tooltestlab.category.domain.model;

import com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException;

import static java.util.Objects.requireNonNull;

public record CategoryName(String value) {

    public CategoryName {
        requireNonNull(value, "카테고리명은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidCategoryNameException.invalidCatName();
        }
    }
}
