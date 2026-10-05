package com.dochiri.tooltestlab.catalog.category.application.port.in.usecase;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;

import java.util.List;
public interface GetCategorySummariesUseCase {

    List<CategorySummary> getAll();

}
