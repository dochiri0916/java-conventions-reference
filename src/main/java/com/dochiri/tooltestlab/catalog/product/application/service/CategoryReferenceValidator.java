package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.port.out.CategoryReferenceValidationPort;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CategoryReferenceValidator {

    private final CategoryReferenceValidationPort categoryReferenceValidationPort;

    public void validate(final Set<CategoryReferenceId> categoryReferences) {
        final Set<String> referenceIds = categoryReferences.stream()
                .map(CategoryReferenceId::value)
                .collect(Collectors.toUnmodifiableSet());

        if (!categoryReferenceValidationPort.allActive(referenceIds)) {
            throw new IllegalArgumentException("");
        }
    }

}
