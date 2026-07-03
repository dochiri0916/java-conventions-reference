package com.dochiri.tooltestlab.product.adapter.out.persistence;

import com.dochiri.tooltestlab.category.adapter.out.persistence.CategoryEntity;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.product.domain.model.Money;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.ProductDescription;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.ProductStatus;
import com.dochiri.tooltestlab.product.domain.model.Stock;

import java.util.List;

final class ProductMapper {

    private ProductMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Product toDomain(final ProductEntity entity) {
        return new Product(
                new ProductId(entity.getProductId()),
                new ProductName(entity.getName()),
                new ProductDescription(entity.getDescription()),
                Money.of(entity.getPriceAmount()),
                Stock.from(entity.getStock()),
                ProductStatus.valueOf(entity.getStatus()));
    }

    static ProductEntity toEntity(final Product domain) {
        return ProductEntity.create(
                domain.id().value(),
                domain.name().value(),
                domain.description().value(),
                domain.price().amount(),
                domain.stock().value(),
                domain.status().name());
    }

    static Category toDomain(final CategoryEntity entity) {
        return new Category(new CategoryId(entity.getCategoryId()), new CategoryName(entity.getName()));
    }

    static CategoryEntity toEntity(final Category domain) {
        return CategoryEntity.create(domain.id().value(), domain.name().value());
    }

    static void updateEntity(final ProductEntity entity, final Product domain) {
        entity.apply(domain);
    }

    static List<Category> toDomainCategories(final List<CategoryEntity> entities) {
        return entities.stream().map(ProductMapper::toDomain).toList();
    }
}
