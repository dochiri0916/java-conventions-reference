package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.port.out.Categories;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryIdSet;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryNameSet;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.isConstraintViolation;

@Repository
@RequiredArgsConstructor
final class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Categories findAllByIds(final CategoryIdSet categoryIds) {
        final Set<CategoryId> ids = categoryIds.values();
        if (ids.isEmpty()) {
            return new Categories(Set.of());
        }
        final List<String> publicIds = ids.stream()
                .map(CategoryId::value)
                .toList();
        return execute(() -> toCategories(categoryJpaRepository.findAllByPublicIdIn(publicIds)));
    }

    @Override
    public Categories findAllByNames(final CategoryNameSet categoryNames) {
        final Set<CategoryName> names = categoryNames.values();
        if (names.isEmpty()) {
            return new Categories(Set.of());
        }
        final List<String> values = names.stream()
                .map(CategoryName::value)
                .toList();
        return execute(() -> toCategories(categoryJpaRepository.findAllByNameIn(values)));
    }

    @Override
    public Categories findAll() {
        return execute(() -> toCategories(categoryJpaRepository.findAll()));
    }

    @Override
    public Category create(final Category category) {
        return execute(() -> {
            try {
                CategoryEntity saved = categoryJpaRepository.saveAndFlush(
                        CategoryMapper.toEntity(category));
                return CategoryMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                throw translateConflict(exception);
            }
        });
    }

    @Override
    public Categories createAll(final Categories categories) {
        final Set<Category> values = categories.values();
        if (values.isEmpty()) {
            return new Categories(Set.of());
        }
        final List<CategoryEntity> entities = values.stream()
                .map(CategoryMapper::toEntity)
                .toList();
        return execute(() -> saveAll(entities));
    }

    private Categories saveAll(final List<CategoryEntity> entities) {
        try {
            return toCategories(categoryJpaRepository.saveAllAndFlush(entities));
        } catch (final DataIntegrityViolationException exception) {
            throw translateConflict(exception);
        }
    }

    private static Categories toCategories(final List<CategoryEntity> entities) {
        final Set<Category> categories = entities.stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet());
        return new Categories(categories);
    }

    private static RuntimeException translateConflict(final DataIntegrityViolationException exception) {
        if (isConstraintViolation(exception, "uk_categories_name")) {
            return new DuplicateCategoryNameException(exception);
        }
        return exception;
    }
}
