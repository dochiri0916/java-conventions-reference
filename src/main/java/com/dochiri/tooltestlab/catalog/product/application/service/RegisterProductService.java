package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.out.CategoryReferenceValidationPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public final class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryReferenceValidationPort categoryReferenceValidationPort;

    @Override
    @Transactional
    public ProductId register(final RegisterProductCommand command) {
        final Set<CategoryReferenceId> categoryReferences = command.categoryIds().stream()
                .map(CategoryReferenceId::new)
                .collect(Collectors.toUnmodifiableSet());

        if (!categoryReferenceValidationPort.allActive(categoryReferences)) {
            throw new IllegalArgumentException();
        }

        final Product product = Product.create(
                command.name(),
                command.description(),
                command.priceAmount(),
                command.stock(),
                categoryReferences
        );

        productRepositoryPort.save(product);

        return product.id();
    }

}
