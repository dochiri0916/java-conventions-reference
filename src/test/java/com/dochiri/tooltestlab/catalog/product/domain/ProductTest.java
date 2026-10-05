package com.dochiri.tooltestlab.catalog.product.domain;

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

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void createsFromValidatedValuesAndProtectsCategories() {
        final ProductName name = ProductName.of("상품 이름");
        final ProductDescription description = ProductDescription.of("상품 설명입니다");
        final Money price = Money.of(1_000);
        final Stock stock = Stock.of(10);
        final CategoryId categoryId = CategoryId.generate();
        final Set<CategoryId> categoryIds = new HashSet<>(Set.of(categoryId));

        final Product product = Product.create(name, description, price, stock, CategoryIds.of(categoryIds));
        categoryIds.clear();

        assertSame(name, product.name());
        assertSame(description, product.description());
        assertSame(price, product.price());
        assertSame(stock, product.stock());
        assertEquals(ProductStatus.PREPARING, product.status());
        assertEquals(CategoryIds.of(Set.of(categoryId)), product.categoryIds());
        assertThrows(UnsupportedOperationException.class, () -> product.categoryIds().values().clear());
    }

    @Test
    void replacesCategoriesWithoutChangingTheOriginalProductOrId() {
        final Product original = Product.create(ProductName.of("상품 이름"), ProductDescription.of("상품 설명입니다"),
                Money.of(1_000), Stock.of(10));
        final CategoryIds categoryIds = CategoryIds.of(Set.of(CategoryId.generate()));

        final Product changed = original.replaceCategories(categoryIds);

        assertTrue(original.categoryIds().isEmpty());
        assertEquals(categoryIds, changed.categoryIds());
        assertEquals(original.id(), changed.id());
        assertNotSame(original, changed);
    }

    @Test
    void rejectsMissingValuesAtTheAggregateBoundary() {
        final ProductName name = ProductName.of("상품 이름");
        final ProductDescription description = ProductDescription.of("상품 설명입니다");
        final Money price = Money.of(1_000);
        final Stock stock = Stock.of(10);

        assertThrows(NullPointerException.class, () -> Product.create(null, description, price, stock));
        assertThrows(NullPointerException.class, () -> Product.create(name, null, price, stock));
        assertThrows(NullPointerException.class, () -> Product.create(name, description, null, stock));
        assertThrows(NullPointerException.class, () -> Product.create(name, description, price, null));
        assertThrows(NullPointerException.class, () -> Product.create(name, description, price, stock, null));
    }
}
