package com.dochiri.tooltestlab.product.adapter.out.persistence;

import com.dochiri.tooltestlab.category.adapter.out.persistence.CategoryEntity;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.domain.model.Money;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.ProductStatus;

import java.util.List;

/**
 * ProductEntity ↔ Product, CategoryEntity ↔ Category 변환 유틸리티.
 */
final class ProductMapper {

    private ProductMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // --- Product ---
    static Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getPublicId(),
                entity.getName(),
                entity.getDescription(),
                Money.of(entity.getPriceAmount()),
                entity.getStock(),
                ProductStatus.valueOf(entity.getStatus()));
    }

    static ProductEntity toEntity(Product domain) {
        return new ProductEntity(
                null,
                domain.publicId(),
                domain.name(),
                domain.description(),
                domain.price().amount(),
                domain.stock(),
                domain.status().name());
    }

    // --- Category ---
    static Category toDomain(CategoryEntity entity) {
        return new Category(entity.getPublicId(), entity.getName());
    }

    static CategoryEntity toEntity(Category domain) {
        return new CategoryEntity(null, domain.publicId(), domain.name());
    }

    static void updateEntity(ProductEntity entity, Product domain) {
        entity.apply(domain);
    }

    static List<Category> toDomainCategories(List<CategoryEntity> entities) {
        return entities.stream().map(ProductMapper::toDomain).toList();
    }
}
