package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    @DisplayName("상품 UUID v7을 표준 36자리 문자열로 변환하고 같은 상품 ID로 복원한다")
    void preservesUuidV7WhenMapping() {
        // given
        final CategoryId categoryId = CategoryId.generate();
        final Product product = Product.create("상품 이름", "상품 설명입니다", 1_000, 10, Set.of(categoryId));

        // when
        final ProductEntity entity = ProductMapper.toEntity(product);
        final Product restored = ProductMapper.toDomain(entity);

        // then
        assertEquals(36, entity.getPublicId().length());
        assertEquals(product.id().productId().toString(), entity.getPublicId());
        assertEquals(7, restored.id().productId().version());
        assertEquals(product.id(), restored.id());
        assertEquals(product.name(), restored.name());
        assertEquals(product.description(), restored.description());
        assertEquals(product.price(), restored.price());
        assertEquals(product.stock(), restored.stock());
        assertEquals(product.status(), restored.status());
        assertEquals(product.categoryIds(), restored.categoryIds());
        assertEquals(Set.of(categoryId.value()), entity.categoryPublicIds());
    }
}
