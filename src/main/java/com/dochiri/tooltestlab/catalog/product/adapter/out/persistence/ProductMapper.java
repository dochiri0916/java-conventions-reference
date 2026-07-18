package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.model.*;
import com.dochiri.tooltestlab.product.domain.model.*;

import java.util.Set;
import java.util.stream.Collectors;

final class ProductMapper {

    private ProductMapper() {
    }

    static Product toDomain(final ProductEntity entity) {
        return Product.restore(
                new ProductId(entity.getPublicId()),
                new ProductName(entity.getName()),
                new ProductDescription(entity.getDescription()),
                new Money(entity.getPriceAmount()),
                new Stock(entity.getStock()),
                entity.getStatus()
        );
    }

    static ProductEntity toEntity(final Product domain) {
        return ProductEntity.create(
                domain.id().value(),
                domain.name().value(),
                domain.description().value(),
                domain.price().amount(),
                domain.stock().value(),
                domain.status()
        );
    }

    static void updateEntity(
            final ProductEntity entity,
            final Product domain
    ) {
        entity.update(
                domain.name().value(),
                domain.description().value(),
                domain.price().amount(),
                domain.stock().value(),
                domain.status()
        );
    }

    private static Set<CategoryReferenceId> toCategoryReferences(final Set<String> categoryReferenceIds) {
        return categoryReferenceIds.stream()
                .map(CategoryReferenceId::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Set<String> toCategoryReferenceValues(final Set<CategoryReferenceId> categoryReferences) {
        return categoryReferences.stream()
                .map(CategoryReferenceId::value)
                .collect(Collectors.toUnmodifiableSet());
    }

}
