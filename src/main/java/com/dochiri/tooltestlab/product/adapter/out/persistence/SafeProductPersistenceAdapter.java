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

/**
 * 동시성 안전 모드용 ProductPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * PESSIMISTIC_WRITE 락으로 동시 수정을 직렬화한다.
 */
@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    SafeProductPersistenceAdapter(ProductJpaRepository productJpaRepository, CategoryJpaRepository categoryJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    @Transactional
    public Product save(Product product, List<Category> categories) {
        // PESSIMISTIC_WRITE로 락을 잡고 조회 → 동시 수정 방지
        Optional<ProductEntity> existing = productJpaRepository.findByPublicIdForUpdate(product.id().value());
        ProductEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            ProductMapper.updateEntity(entity, product);
        } else {
            entity = ProductMapper.toEntity(product);
        }

        if (!categories.isEmpty()) {
            List<String> categoryPublicIds = categories.stream().map(c -> c.id().value()).toList();
            List<CategoryEntity> managedCategories = categoryJpaRepository.findAllByPublicIdIn(categoryPublicIds);
            List<Long> categoryIds = managedCategories.stream().map(CategoryEntity::getId).toList();
            entity.replaceCategoryIds(categoryIds);
        }

        ProductEntity saved = productJpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findByPublicId(String publicId) {
        return productJpaRepository.findByPublicId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(int page, int size) {
        return productJpaRepository.findAll(PageRequest.of(page, size))
                .map(ProductMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByProductPublicId(String productPublicId) {
        ProductEntity product = productJpaRepository.findByPublicId(productPublicId).orElse(null);
        if (product == null) return List.of();
        List<Long> categoryIds = product.getCategoryIds();
        if (categoryIds.isEmpty()) return List.of();
        List<CategoryEntity> categories = categoryJpaRepository.findAllById(categoryIds);
        return ProductMapper.toDomainCategories(categories);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        CategoryEntity entity = ProductMapper.toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findCategoryByPublicId(String publicId) {
        return categoryJpaRepository.findByPublicId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByPublicIds(Set<String> publicIds) {
        return ProductMapper.toDomainCategories(
                categoryJpaRepository.findAllByPublicIdIn(List.copyOf(publicIds)));
    }

    @Override
    public List<Category> findAllCategories() {
        return ProductMapper.toDomainCategories(categoryJpaRepository.findAll());
    }
}
