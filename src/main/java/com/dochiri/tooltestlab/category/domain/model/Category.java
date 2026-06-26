package com.dochiri.tooltestlab.category.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * 카테고리 Entity.
 */
public record Category(String publicId, String name) {

    public Category {
        Objects.requireNonNull(publicId, "publicId는 null일 수 없습니다.");
        Objects.requireNonNull(name, "카테고리명은 null일 수 없습니다.");

        name = name.strip();

        if (publicId.isBlank()) throw new IllegalArgumentException("publicId는 빈 값일 수 없습니다.");
        if (name.isBlank()) throw new IllegalArgumentException("카테고리명은 빈 값일 수 없습니다.");
    }

    public static Category create(String name) {
        return new Category(UUID.randomUUID().toString().replace("-", ""), name);
    }
}
