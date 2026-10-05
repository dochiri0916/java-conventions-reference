package com.dochiri.tooltestlab.catalog.category.application.service;

import com.dochiri.tooltestlab.catalog.category.application.CategoryResultMapper;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummaries;
import com.dochiri.tooltestlab.catalog.category.application.port.in.usecase.GetCategorySummariesUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.out.Categories;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class GetCategorySummariesService implements GetCategorySummariesUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public CategorySummaries getAll() {
        final Categories categories = categoryRepositoryPort.findAll();
        return CategoryResultMapper.toSummaries(categories);
    }
}
