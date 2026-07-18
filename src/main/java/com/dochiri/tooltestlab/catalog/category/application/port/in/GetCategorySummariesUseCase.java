package com.dochiri.tooltestlab.catalog.category.application.port.in;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;

import java.util.List;
import java.util.Set;

public interface GetCategorySummariesUseCase {

    List<CategorySummary> getByPublicIds(Set<String> publicIds);

}
