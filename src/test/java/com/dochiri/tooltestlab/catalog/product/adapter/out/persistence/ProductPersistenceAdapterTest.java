package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductPage;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductSearchCondition;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.global.configuration.QuerydslConfiguration;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import({ProductPersistenceAdapter.class, ProductQueryRepository.class, QuerydslConfiguration.class, ProductPersistenceAdapterTest.AuditingConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ProductPersistenceAdapterTest {

    @Autowired ProductRepositoryPort products;
    @Autowired PlatformTransactionManager transactionManager;

    @Test
    void rejectsDuplicateProductIdAsRepositoryFailure() {
        final Product original = create(product());
        final Product duplicateId = Product.restore(original.id(), ProductName.of("다른 상품"),
                original.description(), original.price(), original.stock(), original.status());

        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> create(duplicateId));

        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
        assertEquals(original.name(), find(original.id()).name());
    }

    @Test
    void preservesSeparateIdentitiesForProductsWithTheSameName() {
        final Product first = create(product());
        final Product second = create(product());

        assertNotEquals(first.id(), second.id());
        assertEquals(first.name(), second.name());
        assertEquals(first.status(), find(first.id()).status());
        assertEquals(second.status(), find(second.id()).status());
    }

    @Test
    void searchesProductsByCombinedConditions() {
        final CategoryId categoryId = CategoryId.generate();
        final String keyword = "검색" + System.nanoTime();
        final Product cheap = create(named(keyword + " 저가", 1_000, CategoryIds.of(Set.of(categoryId))));
        final Product expensive = create(named(keyword + " 고가", 90_000, CategoryIds.of(Set.of(categoryId))));
        create(named(keyword + " 다른 카테고리", 5_000, CategoryIds.empty()));

        final ProductPage byCategoryAndPrice = products.search(new ProductSearchCondition(
                keyword.toUpperCase(), null, categoryId, Money.of(2_000), Money.of(100_000)), 0, 10);
        final ProductPage byStatus = products.search(new ProductSearchCondition(
                keyword, ProductStatus.ON_SALE, null, null, null), 0, 10);
        final ProductPage firstPage = products.search(new ProductSearchCondition(
                keyword, null, categoryId, null, null), 0, 1);

        assertEquals(java.util.List.of(expensive.id()), byCategoryAndPrice.products().stream().map(Product::id).toList());
        assertEquals(1, byCategoryAndPrice.totalCount());
        assertEquals(0, byStatus.totalCount());
        assertEquals(2, firstPage.totalCount());
        assertEquals(1, firstPage.products().size());
        assertNotEquals(cheap.id(), expensive.id());
    }

    @Test
    void rejectsInvalidPagingForSearch() {
        assertThrows(IllegalArgumentException.class,
                () -> products.search(ProductSearchCondition.none(), 0, 101));
        assertThrows(IllegalArgumentException.class,
                () -> products.search(ProductSearchCondition.none(), -1, 10));
    }

    private static Product named(final String name, final long price, final CategoryIds categoryIds) {
        return Product.create(ProductName.of(name), ProductDescription.of("상품 설명입니다"),
                Money.of(price), Stock.of(10), categoryIds);
    }

    private Product create(final Product product) {
        return new TransactionTemplate(transactionManager).execute(status -> products.create(product));
    }

    private Product find(final ProductId productId) {
        return new TransactionTemplate(transactionManager)
                .execute(status -> products.findById(productId).orElseThrow());
    }

    private static Product product() {
        return Product.create(ProductName.of("상품 이름"), ProductDescription.of("상품 설명입니다"),
                Money.of(1_000), Stock.of(10));
    }

    @TestConfiguration
    @EnableJpaAuditing
    static class AuditingConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("test");
        }
    }
}
