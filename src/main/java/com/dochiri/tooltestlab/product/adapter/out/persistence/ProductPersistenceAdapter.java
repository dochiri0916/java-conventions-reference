package com.dochiri.tooltestlab.product.adapter.out.persistence;

import com.dochiri.tooltestlab.category.adapter.out.persistence.CategoryEntity;
import com.dochiri.tooltestlab.category.adapter.out.persistence.CategoryJpaRepository;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Product save(final Product product, final List<Category> categories) {
        final Optional<ProductEntity> existing = productJpaRepository.findByProductId(product.id().value());
        if (existing.isPresent()) {
            final ProductEntity entity = existing.get();
            ProductMapper.updateEntity(entity, product);
            if (!categories.isEmpty()) {
                final List<String> categoryIds = categories.stream().map(c -> c.id().value()).toList();
                entity.replaceCategoryIdValues(categoryIds);
            }
            final ProductEntity saved = productJpaRepository.save(entity);
            return ProductMapper.toDomain(saved);
        }

        final ProductEntity entity = ProductMapper.toEntity(product);
        if (!categories.isEmpty()) {
            final List<String> categoryIds = categories.stream().map(c -> c.id().value()).toList();
            entity.replaceCategoryIdValues(categoryIds);
        }
        final ProductEntity saved = productJpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findByPublicId(final String publicId) {
        return productJpaRepository.findByProductId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(final int page, final int size) {
        return productJpaRepository.findAll(PageRequest.of(page, size))
                .map(ProductMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByProductPublicId(final String productPublicId) {
        final ProductEntity product = productJpaRepository.findByProductId(productPublicId).orElse(null);
        if (product == null) {
            return List.of();
        }
        final List<String> categoryIds = product.getCategoryIdValues();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        final List<CategoryEntity> categories = categoryJpaRepository.findAllByCategoryIdIn(categoryIds);
        return ProductMapper.toDomainCategories(categories);
    }

    @Override
    public Category saveCategory(final Category category) {
        final CategoryEntity entity = ProductMapper.toEntity(category);
        final CategoryEntity saved = categoryJpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findCategoryByPublicId(final String publicId) {
        return categoryJpaRepository.findByCategoryId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByPublicIds(final Set<String> publicIds) {
        return ProductMapper.toDomainCategories(
                categoryJpaRepository.findAllByCategoryIdIn(List.copyOf(publicIds)));
    }

    @Override
    public List<Category> findAllCategories() {
        return ProductMapper.toDomainCategories(categoryJpaRepository.findAll());
    }
}
