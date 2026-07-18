package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dochiri.tooltestlab.category.adapter.out.persistence.QCategoryEntity.categoryEntity;
import static com.dochiri.tooltestlab.product.adapter.out.persistence.QProductEntity.productEntity;

@Repository
@RequiredArgsConstructor
public final class ProductQueryAdapter implements ProductQueryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ProductDetail> findDetailByPublicId(final String publicId) {
        final ProductDetailProjection product = queryFactory
                .select(Projections.constructor(
                        ProductDetailProjection.class,
                        productEntity.publicId,
                        productEntity.name,
                        productEntity.description,
                        productEntity.priceAmount,
                        productEntity.stock,
                        productEntity.status
                ))
                .from(productEntity)
                .where(productEntity.publicId.eq(publicId))
                .fetchOne();

        if (product == null) {
            return Optional.empty();
        }

        final List<String> categoryNames = queryFactory
                .select(categoryEntity.name)
                .from(p)

        return Optional.empty();
    }
}
