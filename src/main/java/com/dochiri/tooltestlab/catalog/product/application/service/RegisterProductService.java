package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.catalog.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public final class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional
    public ProductId register(final RegisterProductCommand command) {
        final Set<Category> existingCategories =
                findExistingCategories(command.existingCategoryIds());

        final Set<Category> resolvedCategories =
                resolveNewCategories(command.newCategoryNames());

        final Set<CategoryId> categoryIds = Stream.concat(
                        existingCategories.stream(),
                        resolvedCategories.stream()
                )
                .map(Category::id)
                .collect(Collectors.toUnmodifiableSet());

        final Product product = Product.create(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                categoryIds
        );

        return productRepositoryPort.create(product).id();
    }

    private Set<Category> findExistingCategories(
            final Set<CategoryId> requestedCategoryIds
    ) {
        if (requestedCategoryIds.isEmpty()) {
            return Set.of();
        }

        final Set<Category> categories =
                categoryRepositoryPort.findAllByIds(requestedCategoryIds);

        validateAllCategoriesExist(
                requestedCategoryIds,
                categories
        );
        validateAllCategoriesActive(categories);

        return categories;
    }

    private Set<Category> resolveNewCategories(final Set<String> requestedCategoryNames) {
        if (requestedCategoryNames.isEmpty()) {
            return Set.of();
        }

        final Set<CategoryName> categoryNames = requestedCategoryNames.stream()
                .map(CategoryName::new)
                .collect(Collectors.toUnmodifiableSet());

        final Set<Category> foundCategories =
                categoryRepositoryPort.findAllByNames(categoryNames);

        validateAllCategoriesActive(foundCategories);

        final Set<CategoryName> foundCategoryNames = foundCategories.stream()
                .map(Category::name)
                .collect(Collectors.toUnmodifiableSet());

        final Set<Category> categoriesToCreate = categoryNames.stream()
                .filter(categoryName -> !foundCategoryNames.contains(categoryName))
                .map(categoryName -> Category.create(categoryName.value()))
                .collect(Collectors.toUnmodifiableSet());

        final Set<Category> createdCategories =
                categoriesToCreate.isEmpty()
                        ? Set.of()
                        : categoryRepositoryPort.createAll(categoriesToCreate);

        return Stream.concat(
                        foundCategories.stream(),
                        createdCategories.stream()
                )
                .collect(Collectors.toUnmodifiableSet());
    }

    private static void validateAllCategoriesExist(final Set<CategoryId> requestedCategoryIds, final Set<Category> foundCategories) {
        final Set<CategoryId> foundCategoryIds = foundCategories.stream()
                .map(Category::id)
                .collect(Collectors.toUnmodifiableSet());

        final Set<CategoryId> missingCategoryIds = requestedCategoryIds.stream()
                .filter(categoryId -> !foundCategoryIds.contains(categoryId))
                .collect(Collectors.toUnmodifiableSet());

        if (!missingCategoryIds.isEmpty()) {
            throw new IllegalArgumentException("");
        }
    }

    private static void validateAllCategoriesActive(final Set<Category> categories) {
        final Set<CategoryId> unavailableCategoryIds = categories.stream()
                .filter(category -> !category.state().isActive())
                .map(Category::id)
                .collect(Collectors.toUnmodifiableSet());

        if (!unavailableCategoryIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

}
