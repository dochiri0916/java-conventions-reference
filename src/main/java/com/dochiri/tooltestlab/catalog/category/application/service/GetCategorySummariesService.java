package com.dochiri.tooltestlab.catalog.category.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.in.GetCategorySummariesUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
@Service
@RequiredArgsConstructor
public final class GetCategorySummariesService implements GetCategorySummariesUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<CategorySummary> getAll() {
        return categoryRepositoryPort.findAll().stream()
                .map(category -> new CategorySummary(
                        category.id().value(),
                        category.name().value()
                ))
                .sorted(Comparator.comparing(CategorySummary::name))
                .toList();
    }
}
