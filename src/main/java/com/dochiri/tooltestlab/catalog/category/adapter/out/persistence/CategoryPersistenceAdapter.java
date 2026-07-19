package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Set<Category> findAllByIds(Set<CategoryId> categoryIds) {
        if (categoryIds.isEmpty()) {
            return Set.of();
        }

        return categoryJpaRepository.findAllByPublicIdIn(
                        categoryIds.stream().map(CategoryId::value).toList()
                ).stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Category> findAllByNames(Set<CategoryName> categoryNames) {
        if (categoryNames.isEmpty()) {
            return Set.of();
        }

        return categoryJpaRepository.findAllByNameIn(
                        categoryNames.stream().map(CategoryName::value).toList()
                ).stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Category> findAll() {
        return categoryJpaRepository.findAll().stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Category create(final Category category) {
        final CategoryEntity saved = categoryJpaRepository.save(CategoryMapper.toEntity(category));

        return CategoryMapper.toDomain(saved);
    }

    @Override
    public Set<Category> createAll(Set<Category> categories) {
        if (categories.isEmpty()) {
            return Set.of();
        }

        return categoryJpaRepository.saveAll(
                        categories.stream().map(CategoryMapper::toEntity).toList()
                ).stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }
}
