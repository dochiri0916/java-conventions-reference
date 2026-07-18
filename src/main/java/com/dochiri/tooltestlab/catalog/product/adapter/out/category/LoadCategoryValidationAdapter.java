package com.dochiri.tooltestlab.catalog.product.adapter.out.category;

import com.dochiri.tooltestlab.catalog.category.application.port.in.CategoryQueryUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.CategoryReferenceValidationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoadCategoryValidationAdapter implements CategoryReferenceValidationPort {

    private final CategoryQueryUseCase categoryQueryUseCase;

    @Override
    public boolean allActive(Set<CategoryReferenceId> categoryReferences) {
        final Set<String> categoryIds = categoryReferences.stream()
                .map(CategoryReferenceId::value)
                .collect(Collectors.toUnmodifiableSet());

        return categoryQueryUseCase.allActive(categoryIds);
    }
}
