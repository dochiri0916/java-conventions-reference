package com.dochiri.tooltestlab.catalog.category.application.port.in;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.GetCategoryResult;

import java.util.List;
import java.util.Set;

public interface GetCategoriesUseCase {

    List<GetCategoryResult> getByPublicIds(Set<String> publicIds);

}
