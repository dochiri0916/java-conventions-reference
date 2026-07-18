package com.dochiri.tooltestlab.catalog.category.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.in.GetCategorySummariesUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GetCategorySummariesService implements GetCategorySummariesUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<CategorySummary> getByPublicIds(final Set<String> publicIds) {
        if (publicIds.isEmpty()) {
            return List.of();
        }

        return categoryRepositoryPort.;
    }
}
