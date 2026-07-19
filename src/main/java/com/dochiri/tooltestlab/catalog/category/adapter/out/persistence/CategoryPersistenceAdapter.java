package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(final Category category) {
        return null;
    }

    @Override
    public Optional<Category> findByPublicId(final String publicId) {
        return Optional.empty();
    }

    @Override
    public List<Category> findAllByPublicIds(Set<String> publicIds) {
        return List.of();
    }

    @Override
    public Set<Category> findAllByIds(Set<CategoryId> categoryIds) {
        return Set.of();
    }

}
