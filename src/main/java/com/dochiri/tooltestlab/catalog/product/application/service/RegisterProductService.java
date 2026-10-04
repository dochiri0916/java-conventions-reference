package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.application.exception.CategoriesNotActiveException;
import com.dochiri.tooltestlab.catalog.product.application.exception.CategoriesNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.application.port.out.IntegrationEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public final class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public RegisterProductResult register(final RegisterProductCommand command) {
        final Set<CategoryId> categoryIds = command.categoryIds().stream()
                .map(CategoryId::parse)
                .collect(Collectors.toUnmodifiableSet());
        final Set<Category> categories = categoryIds.isEmpty()
                ? Set.of()
                : categoryRepositoryPort.findAllByIds(categoryIds);

        validateAllCategoriesExist(categoryIds, categories);
        validateAllCategoriesActive(categories);
        final Product product = Product.create(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                categoryIds
        );
        final Product created = productRepositoryPort.create(product);
        integrationEventPort.publish(CatalogEventMapper.toIntegration(created.changedEvent()));

        return new RegisterProductResult(
                created.id().value(),
                created.name().value(),
                created.price().amount(),
                created.stock().value(),
                created.status().name(),
                categories.stream().map(category -> category.name().value()).sorted().toList()
        );
    }

    private static void validateAllCategoriesExist(
            final Set<CategoryId> requestedCategoryIds,
            final Set<Category> foundCategories
    ) {
        final Set<CategoryId> foundCategoryIds = foundCategories.stream()
                .map(Category::id)
                .collect(Collectors.toUnmodifiableSet());

        final Set<String> missingCategoryIds = requestedCategoryIds.stream()
                .filter(categoryId -> !foundCategoryIds.contains(categoryId))
                .map(CategoryId::value)
                .collect(Collectors.toUnmodifiableSet());

        if (!missingCategoryIds.isEmpty()) {
            throw new CategoriesNotFoundException();
        }
    }

    private static void validateAllCategoriesActive(final Set<Category> categories) {
        final Set<String> unavailableCategoryIds = categories.stream()
                .filter(category -> !category.state().isActive())
                .map(category -> category.id().value())
                .collect(Collectors.toUnmodifiableSet());

        if (!unavailableCategoryIds.isEmpty()) {
            throw new CategoriesNotActiveException();
        }
    }

}
