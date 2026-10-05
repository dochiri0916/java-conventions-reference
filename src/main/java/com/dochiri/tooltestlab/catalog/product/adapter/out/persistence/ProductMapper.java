package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.github.f4b6a3.uuid.UuidCreator;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

final class ProductMapper {

    private ProductMapper() {
    }

    static Product toDomain(final ProductEntity entity, final Collection<ProductCategoryEntity> categories) {
        return Product.restore(
                ProductId.parse(entity.getPublicId()),
                ProductName.of(entity.getName()),
                ProductDescription.of(entity.getDescription()),
                Money.of(entity.getPriceAmount()),
                Stock.of(entity.getStock()),
                ProductStatus.valueOf(entity.getStatus()),
                toCategoryIds(categories)
        );
    }

    static Product toDomain(final ProductRow row, final Collection<ProductCategoryEntity> categories) {
        return Product.restore(
                ProductId.parse(row.publicId()),
                ProductName.of(row.name()),
                ProductDescription.of(row.description()),
                Money.of(row.priceAmount()),
                Stock.of(row.stock()),
                ProductStatus.valueOf(row.status()),
                toCategoryIds(categories)
        );
    }

    static ProductEntity toEntity(final Product domain) {
        return ProductEntity.create(
                domain.id().value(),
                domain.name().value(),
                domain.description().value(),
                domain.price().amount(),
                domain.stock().value(),
                domain.status().name()
        );
    }

    static List<ProductCategoryEntity> toCategoryEntities(final Product domain) {
        return domain.categoryIds().values().stream()
                .map(categoryId -> ProductCategoryEntity.create(
                        UuidCreator.getTimeOrderedEpoch().toString(),
                        domain.id().value(),
                        categoryId.value()))
                .toList();
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
                domain.status().name()
        );
    }

    private static CategoryIds toCategoryIds(final Collection<ProductCategoryEntity> categories) {
        return CategoryIds.of(categories.stream()
                .map(ProductCategoryEntity::getCategoryId)
                .map(CategoryId::parse)
                .collect(Collectors.toUnmodifiableSet()));
    }

}
