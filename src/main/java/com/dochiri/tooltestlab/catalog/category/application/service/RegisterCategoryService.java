package com.dochiri.tooltestlab.catalog.category.application.service;

import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.port.in.RegisterCategoryUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.RegisterCategoryResult;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public final class RegisterCategoryService implements RegisterCategoryUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional
    public RegisterCategoryResult register(final RegisterCategoryCommand command) {
        final CategoryName categoryName = new CategoryName(command.name());
        final boolean exists = categoryRepositoryPort.findAllByNames(Set.of(categoryName))
                .stream()
                .anyMatch(category -> category.name().equals(categoryName));
        if (exists) {
            throw DuplicateCategoryNameException.duplicateCategoryName(categoryName);
        }

        final Category saved = categoryRepositoryPort.create(Category.create(categoryName.value()));
        return new RegisterCategoryResult(saved.id().value(), saved.name().value());
    }
}
