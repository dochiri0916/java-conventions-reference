package com.dochiri.tooltestlab.catalog.product.application.port.in.command;

import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;

import java.util.Set;

import static java.util.Objects.requireNonNull;

public record RegisterProductCommand(
        String name,
        String description,
        int price,
        int stock,
        Set<CategoryId> existingCategoryIds,
        Set<String> newCategoryNames
) {
    public RegisterProductCommand {
        existingCategoryIds = Set.copyOf(
                requireNonNull(
                        existingCategoryIds,
                        "기존 카테고리 ID 목록은 null일 수 없습니다."
                )
        );
        newCategoryNames = Set.copyOf(
                requireNonNull(
                        newCategoryNames,
                        "신규 카테고리명 목록은 null일 수 없습니다."
                )
        );
    }
}
