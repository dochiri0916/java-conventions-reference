package com.dochiri.tooltestlab.catalog.category.domain.vo;

import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryIdRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryIdException;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class CategoryId {

    private final UUID value;

    private CategoryId(final UUID value) {
        if (value == null) {
            throw new CategoryIdRequiredException();
        }

        if (value.version() != 7) {
            throw new InvalidCategoryIdException();
        }
        this.value = value;
    }

    public static CategoryId generate() {
        return new CategoryId(
                UuidCreator.getTimeOrderedEpoch()
        );
    }

    public static CategoryId of(final UUID value) {
        return new CategoryId(value);
    }

    public static CategoryId parse(final String value) {
        if (value == null) {
            throw new CategoryIdRequiredException();
        }

        final UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidCategoryIdException();
        }

        if (!uuid.toString().equalsIgnoreCase(value)) {
            throw new InvalidCategoryIdException();
        }

        return of(uuid);
    }

    public UUID categoryId() {
        return value;
    }

    public String value() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final CategoryId other)) {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryId[value=" + value + "]";
    }
}
