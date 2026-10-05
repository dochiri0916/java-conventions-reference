package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.application.exception.CategoriesNotActiveException;
import com.dochiri.tooltestlab.catalog.product.application.exception.CategoriesNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.catalog.product.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.product.application.ProductResultMapper;
import com.dochiri.tooltestlab.catalog.product.application.port.out.IntegrationEventPort;
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
        final CategoryIds categoryIds = toCategoryIds(command);
        final Set<Category> categories = findCategories(categoryIds);

        validateAllCategoriesExist(categoryIds, categories);
        validateAllCategoriesActive(categories);
        final Product created = productRepositoryPort.create(toProduct(command, categoryIds));
        integrationEventPort.publish(CatalogEventMapper.toIntegration(created.changedEvent()));

        return ProductResultMapper.toRegisterResult(created, categories);
    }

    private static CategoryIds toCategoryIds(final RegisterProductCommand command) {
        return CategoryIds.of(command.categoryIds().stream()
                .map(CategoryId::parse)
                .collect(Collectors.toUnmodifiableSet()));
    }

    private Set<Category> findCategories(final CategoryIds categoryIds) {
        if (categoryIds.isEmpty()) {
            return Set.of();
        }
        return categoryRepositoryPort.findAllByIds(categoryIds.values());
    }

    private static Product toProduct(final RegisterProductCommand command, final CategoryIds categoryIds) {
        return Product.create(
                ProductName.of(command.name()),
                ProductDescription.of(command.description()),
                Money.of(command.price()),
                Stock.of(command.stock()),
                categoryIds
        );
    }

    private static void validateAllCategoriesExist(
            final CategoryIds requestedCategoryIds,
            final Set<Category> foundCategories
    ) {
        final Set<CategoryId> foundCategoryIds = foundCategories.stream()
                .map(Category::id)
                .collect(Collectors.toUnmodifiableSet());

        final Set<String> missingCategoryIds = requestedCategoryIds.values().stream()
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
