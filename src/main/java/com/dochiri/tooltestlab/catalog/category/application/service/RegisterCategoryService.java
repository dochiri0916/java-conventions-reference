package com.dochiri.tooltestlab.catalog.category.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.in.RegisterCategoryUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.RegisterCategoryResult;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class RegisterCategoryService implements RegisterCategoryUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional
    public RegisterCategoryResult register(final RegisterCategoryCommand command) {
        final Category saved = productRepositoryPort.saveCategory(Category.create(new CategoryName(command.name())));
        return new RegisterCategoryResult(saved.id().value(), saved.name().value());
    }
}
