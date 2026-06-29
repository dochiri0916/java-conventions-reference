package com.dochiri.tooltestlab.product.adapter.out.persistence;

import com.dochiri.tooltestlab.category.adapter.out.persistence.CategoryEntity;
import com.dochiri.tooltestlab.category.adapter.out.persistence.CategoryJpaRepository;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    ProductPersistenceAdapter(final ProductJpaRepository productJpaRepository, final CategoryJpaRepository categoryJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    @Transactional
    public Product save(final Product product, final List<Category> categories) {
        final Optional<ProductEntity> existing = productJpaRepository.findByPublicId(product.id().value());
        final ProductEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            ProductMapper.updateEntity(entity, product);
        } else {
            entity = ProductMapper.toEntity(product);
        }

        if (!categories.isEmpty()) {
            final List<String> categoryPublicIds = categories.stream().map(c -> c.id().value()).toList();
            final List<CategoryEntity> managedCategories = categoryJpaRepository.findAllByPublicIdIn(categoryPublicIds);
            final List<Long> categoryIds = managedCategories.stream().map(CategoryEntity::getId).toList();
            entity.replaceCategoryIds(categoryIds);
        }

        final ProductEntity saved = productJpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findByPublicId(final String publicId) {
        return productJpaRepository.findByPublicId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(final int page, final int size) {
        return productJpaRepository.findAll(PageRequest.of(page, size))
                .map(ProductMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByProductPublicId(final String productPublicId) {
        final ProductEntity product = productJpaRepository.findByPublicId(productPublicId).orElse(null);
        if (product == null) {
            return List.of();
        }
        final List<Long> categoryIds = product.getCategoryIds();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        final List<CategoryEntity> categories = categoryJpaRepository.findAllById(categoryIds);
        return ProductMapper.toDomainCategories(categories);
    }

    @Override
    @Transactional
    public Category saveCategory(final Category category) {
        final CategoryEntity entity = ProductMapper.toEntity(category);
        final CategoryEntity saved = categoryJpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findCategoryByPublicId(final String publicId) {
        return categoryJpaRepository.findByPublicId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByPublicIds(final Set<String> publicIds) {
        return ProductMapper.toDomainCategories(
                categoryJpaRepository.findAllByPublicIdIn(List.copyOf(publicIds)));
    }

    @Override
    public List<Category> findAllCategories() {
        return ProductMapper.toDomainCategories(categoryJpaRepository.findAll());
    }
}
