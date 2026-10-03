package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;

import java.util.Set;
import java.util.stream.Collectors;

final class ProductMapper {

    private ProductMapper() {
    }

    static Product toDomain(final ProductEntity entity) {
        return Product.restore(
                ProductId.parse(entity.getPublicId()),
                ProductName.of(entity.getName()),
                ProductDescription.of(entity.getDescription()),
                Money.of(entity.getPriceAmount()),
                Stock.of(entity.getStock()),
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
                .map(CategoryId::parse)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Set<String> toCategoryPublicIds(final Set<CategoryId> categoryIds) {
        return categoryIds.stream()
                .map(CategoryId::value)
                .collect(Collectors.toUnmodifiableSet());
    }

}
