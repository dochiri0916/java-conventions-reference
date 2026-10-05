package com.dochiri.tooltestlab.catalog.category.application.port.in.usecase;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummaries;

@FunctionalInterface
public interface GetCategorySummariesUseCase {

    CategorySummaries getAll();

}
