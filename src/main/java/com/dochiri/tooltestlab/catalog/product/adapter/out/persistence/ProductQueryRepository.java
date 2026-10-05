package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.QProductCategoryEntity.productCategoryEntity;
import static com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.QProductEntity.productEntity;

@Component
final class ProductQueryRepository {

    private static final int MAX_PAGE_SIZE = 100;

    private final JPAQueryFactory queryFactory;

    ProductQueryRepository(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    List<ProductRow> search(final ProductSearchCondition condition, final int page, final int size) {
        validatePaging(page, size);

        return queryFactory
                .select(Projections.constructor(ProductRow.class,
                        productEntity.publicId,
                        productEntity.name,
                        productEntity.description,
                        productEntity.priceAmount,
                        productEntity.stock,
                        productEntity.status
                ))
                .from(productEntity)
                .where(
                        keywordContains(condition),
                        statusEq(condition),
                        categoryEq(condition),
                        priceGoe(condition),
                        priceLoe(condition)
                )
                .orderBy(productEntity.createdAt.desc(), productEntity.id.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    long count(final ProductSearchCondition condition) {
        final Long count = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(
                        keywordContains(condition),
                        statusEq(condition),
                        categoryEq(condition),
                        priceGoe(condition),
                        priceLoe(condition)
                )
                .fetchOne();
        return count == null ? 0L : count;
    }

    private static void validatePaging(final int page, final int size) {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("page는 0 이상, size는 1 이상 " + MAX_PAGE_SIZE + " 이하여야 합니다.");
        }
    }

    private static BooleanExpression keywordContains(final ProductSearchCondition condition) {
        if (condition.keyword() == null || condition.keyword().isBlank()) {
            return null;
        }
        return productEntity.name.containsIgnoreCase(condition.keyword().strip());
    }

    private static BooleanExpression statusEq(final ProductSearchCondition condition) {
        if (condition.status() == null) {
            return null;
        }
        return productEntity.status.eq(condition.status().name());
    }

    private static BooleanExpression categoryEq(final ProductSearchCondition condition) {
        if (condition.categoryId() == null) {
            return null;
        }
        return JPAExpressions.selectOne()
                .from(productCategoryEntity)
                .where(
                        productCategoryEntity.productId.eq(productEntity.publicId),
                        productCategoryEntity.categoryId.eq(condition.categoryId().value())
                )
                .exists();
    }

    private static BooleanExpression priceGoe(final ProductSearchCondition condition) {
        if (condition.minPrice() == null) {
            return null;
        }
        return productEntity.priceAmount.goe(condition.minPrice().amount());
    }

    private static BooleanExpression priceLoe(final ProductSearchCondition condition) {
        if (condition.maxPrice() == null) {
            return null;
        }
        return productEntity.priceAmount.loe(condition.maxPrice().amount());
    }

}
