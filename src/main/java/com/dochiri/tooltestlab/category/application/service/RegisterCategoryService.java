package com.dochiri.tooltestlab.category.application.service;

import com.dochiri.tooltestlab.category.application.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryUseCase;
import com.dochiri.tooltestlab.category.application.result.RegisterCategoryResult;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterCategoryService implements RegisterCategoryUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public RegisterCategoryService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public RegisterCategoryResult register(RegisterCategoryCommand command) {
        Category saved = productRepositoryPort.saveCategory(Category.create(command.name()));
        return new RegisterCategoryResult(saved.publicId(), saved.name());
    }
}
