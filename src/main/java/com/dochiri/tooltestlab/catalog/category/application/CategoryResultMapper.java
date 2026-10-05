package com.dochiri.tooltestlab.catalog.category.application;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummaries;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.RegisterCategoryResult;
import com.dochiri.tooltestlab.catalog.category.application.port.out.Categories;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class CategoryResultMapper {

    private CategoryResultMapper() {
    }

    public static RegisterCategoryResult toRegisterResult(final Category category) {
        final CategoryId id = category.id();
        final CategoryName name = category.name();
        return new RegisterCategoryResult(id.value(),
                name.value());
    }

    public static CategorySummaries toSummaries(final Categories found) {
        final Set<Category> categories = found.values();
        final List<CategorySummary> summaries = categories.stream()
                .map(CategoryResultMapper::toSummary)
                .sorted(Comparator.comparing(CategorySummary::name))
                .toList();
        return new CategorySummaries(summaries);
    }

    private static CategorySummary toSummary(final Category category) {
        final CategoryId id = category.id();
        final CategoryName name = category.name();
        return new CategorySummary(id.value(),
                name.value());
    }
}
