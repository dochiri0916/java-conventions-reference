package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;

@Repository
@RequiredArgsConstructor
class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Set<Category> findAllByIds(final Set<CategoryId> categoryIds) {
        return execute(() -> {
            if (categoryIds.isEmpty()) {
                return Set.of();
            }

            return categoryJpaRepository.findAllByPublicIdIn(
                            categoryIds.stream().map(CategoryId::value).toList()
                    ).stream()
                    .map(CategoryMapper::toDomain)
                    .collect(Collectors.toUnmodifiableSet());
        });
    }

    @Override
    public Set<Category> findAllByNames(final Set<CategoryName> categoryNames) {
        return execute(() -> {
            if (categoryNames.isEmpty()) {
                return Set.of();
            }

            return categoryJpaRepository.findAllByNameIn(
                            categoryNames.stream().map(CategoryName::value).toList()
                    ).stream()
                    .map(CategoryMapper::toDomain)
                    .collect(Collectors.toUnmodifiableSet());
        });
    }

    @Override
    public Set<Category> findAll() {
        return execute(() -> categoryJpaRepository.findAll().stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public Category create(final Category category) {
        return execute(() -> {
            final CategoryEntity saved = categoryJpaRepository.save(CategoryMapper.toEntity(category));

            return CategoryMapper.toDomain(saved);
        });
    }

    @Override
    public Set<Category> createAll(final Set<Category> categories) {
        return execute(() -> {
            if (categories.isEmpty()) {
                return Set.of();
            }

            return categoryJpaRepository.saveAll(
                            categories.stream().map(CategoryMapper::toEntity).toList()
                    ).stream()
                    .map(CategoryMapper::toDomain)
                    .collect(Collectors.toUnmodifiableSet());
        });
    }
}
