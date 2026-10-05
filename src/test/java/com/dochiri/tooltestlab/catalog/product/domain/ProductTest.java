package com.dochiri.tooltestlab.catalog.product.domain;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    @DisplayName("검증된 값으로 상품을 만들고 카테고리를 보호한다")
    void creates_from_validated_values_and_protects_categories() {
        // given
        final ProductName name = ProductName.of("상품 이름");
        final ProductDescription description = ProductDescription.of("상품 설명입니다");
        final Money price = Money.of(1_000);
        final Stock stock = Stock.of(10);
        final CategoryId categoryId = CategoryId.generate();
        final Set<CategoryId> categoryIds = new HashSet<>(Set.of(categoryId));
        final Product product = Product.create(name, description, price, stock, CategoryIds.of(categoryIds));
        categoryIds.clear();

        // when & then
        assertSame(name, product.name());
        assertSame(description, product.description());
        assertSame(price, product.price());
        assertSame(stock, product.stock());
        assertEquals(ProductStatus.PREPARING, product.status());
        assertEquals(CategoryIds.of(Set.of(categoryId)), product.categoryIds());
        assertThrows(UnsupportedOperationException.class, () -> product.categoryIds().values().clear());
    }

    @Test
    @DisplayName("카테고리를 교체해도 원본 상품과 ID는 바뀌지 않는다")
    void replaces_categories_without_changing_the_original_product_or_id() {
        // given
        final Product original = Product.create(ProductName.of("상품 이름"), ProductDescription.of("상품 설명입니다"),
                Money.of(1_000), Stock.of(10));
        final CategoryIds categoryIds = CategoryIds.of(Set.of(CategoryId.generate()));
        final Product changed = original.replaceCategories(categoryIds);

        // when & then
        assertTrue(original.categoryIds().isEmpty());
        assertEquals(categoryIds, changed.categoryIds());
        assertEquals(original.id(), changed.id());
        assertNotSame(original, changed);
    }

    @Test
    @DisplayName("애그리거트 경계에서 누락된 값을 거부한다")
    void rejects_missing_values_at_the_aggregate_boundary() {
        // given
        final ProductName name = ProductName.of("상품 이름");
        final ProductDescription description = ProductDescription.of("상품 설명입니다");
        final Money price = Money.of(1_000);
        final Stock stock = Stock.of(10);

        // when & then
        assertThrows(NullPointerException.class, () -> Product.create(null, description, price, stock));
        assertThrows(NullPointerException.class, () -> Product.create(name, null, price, stock));
        assertThrows(NullPointerException.class, () -> Product.create(name, description, null, stock));
        assertThrows(NullPointerException.class, () -> Product.create(name, description, price, null));
        assertThrows(NullPointerException.class, () -> Product.create(name, description, price, stock, null));
    }
}
