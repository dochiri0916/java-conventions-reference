package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.command.RegisterProductCommand;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.application.result.RegisterProductResult;
import com.dochiri.tooltestlab.product.domain.model.Money;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public RegisterProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public RegisterProductResult register(RegisterProductCommand command) {
        Product product = Product.create(
                command.name(),
                command.description(),
                Money.of(command.priceAmount()),
                command.stock());

        List<Category> categories = List.of();
        if (!command.categoryIds().isEmpty()) {
            categories = productRepositoryPort.findCategoriesByPublicIds(new HashSet<>(command.categoryIds()));
        }

        Product saved = productRepositoryPort.save(product, categories);

        List<String> categoryNames = categories.stream().map(Category::name).toList();
        return new RegisterProductResult(
                saved.id().value(), saved.name().value(), saved.price().amount(),
                saved.stock().value(), saved.status().name(), categoryNames);
    }
}
