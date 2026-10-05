package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    @Test
    @DisplayName("상품 UUID v7을 표준 36자리 문자열로 변환하고 같은 상품 ID로 복원한다")
    void preserves_uuid_v7_when_mapping() {
        // given
        final CategoryId categoryId = CategoryId.generate();
        final Product product = Product.create(ProductName.of("상품 이름"), ProductDescription.of("상품 설명입니다"),
                Money.of(1_000), Stock.of(10), CategoryIds.of(Set.of(categoryId)));

        // when
        final ProductEntity entity = ProductMapper.toEntity(product);
        final List<ProductCategoryEntity> categories = ProductMapper.toCategoryEntities(product);
        final Product restored = ProductMapper.toDomain(entity, categories);

        // then
        assertEquals(36, entity.getPublicId().length());
        assertEquals(product.id().productId().toString(), entity.getPublicId());
        assertEquals(7, restored.id().productId().version());
        assertEquals(product.id(), restored.id());
        assertEquals(product.name(), restored.name());
        assertEquals(product.description(), restored.description());
        assertEquals(product.price(), restored.price());
        assertEquals(product.stock(), restored.stock());
        assertEquals(product.status().name(), entity.getStatus());
        assertEquals(product.status(), restored.status());
        assertEquals(product.categoryIds(), restored.categoryIds());
        assertEquals(1, categories.size());
        assertEquals(product.id().value(), categories.get(0).getProductId());
        assertEquals(categoryId.value(), categories.get(0).getCategoryId());
    }

    @Test
    @DisplayName("상품 상태와 재고 변경을 문자열 저장 형식으로 반영하고 복원한다")
    void maps_updated_stock_and_status() {
        // given
        final Product product = Product.create(ProductName.of("상품 이름"), ProductDescription.of("상품 설명입니다"),
                Money.of(1_000), Stock.of(10));
        final ProductEntity entity = ProductMapper.toEntity(product);
        final Product changed = product.startSale().decreaseStock(3);
        ProductMapper.updateEntity(entity, changed);
        final Product restored = ProductMapper.toDomain(entity, List.of());

        // when & then
        assertEquals(changed.status().name(), entity.getStatus());
        assertEquals(changed.id(), restored.id());
        assertEquals(changed.status(), restored.status());
        assertEquals(changed.stock(), restored.stock());
        assertEquals(10, product.stock().value());
    }
}
