package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductCommand;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductResult;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.shared.model.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public final class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public RegisterProductService(final ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public RegisterProductResult register(final RegisterProductCommand command) {
        final Product product = Product.create(
                command.name(),
                command.description(),
                Money.of(command.priceAmount()),
                command.stock());

        List<Category> categories = List.of();
        if (!command.categoryIds().isEmpty()) {
            categories = productRepositoryPort.findCategoriesByPublicIds(new HashSet<>(command.categoryIds()));
        }

        final Product saved = productRepositoryPort.save(product, categories);

        final List<String> categoryNames = categories.stream().map(c -> c.name().value()).toList();
        return new RegisterProductResult(
                saved.id().value(), saved.name().value(), saved.price().amount(),
                saved.stock().value(), saved.status().name(), categoryNames);
    }
}
