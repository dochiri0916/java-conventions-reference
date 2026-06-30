package com.dochiri.tooltestlab.category.application.service;

import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryCommand;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryResult;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryUseCase;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public final class RegisterCategoryService implements RegisterCategoryUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public RegisterCategoryService(final ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public RegisterCategoryResult register(final RegisterCategoryCommand command) {
        final Category saved = productRepositoryPort.saveCategory(Category.create(new CategoryName(command.name())));
        return new RegisterCategoryResult(saved.id().value(), saved.name().value());
    }
}
