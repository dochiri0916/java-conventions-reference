package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.product.domain.model.*;

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
                entity.getStatus(),
                toCategoryIds(entity.categoryPublicIds())
        );
    }

    static ProductEntity toEntity(final Product domain) {
        return ProductEntity.create(
                domain.id().value(),
                domain.name().value(),
                domain.description().value(),
                domain.price().amount(),
                domain.stock().value(),
                domain.status(),
                toCategoryPublicIds(domain.categoryIds())
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
                domain.status(),
                toCategoryPublicIds(domain.categoryIds())
        );
    }

    private static Set<CategoryId> toCategoryIds(final Set<String> categoryPublicIds) {
        return categoryPublicIds.stream()
                .map(CategoryId::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Set<String> toCategoryPublicIds(final Set<CategoryId> categoryIds) {
        return categoryIds.stream()
                .map(CategoryId::value)
                .collect(Collectors.toUnmodifiableSet());
    }

}
