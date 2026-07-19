package com.dochiri.tooltestlab.catalog.product.application.exception;

import com.dochiri.tooltestlab.global.exception.ApplicationException;

import java.util.Map;
import java.util.Set;

public final class ProductApplicationException extends ApplicationException {

    public static ProductApplicationException notFound(
            final String productId
    ) {
        final ProductApplicationErrorCode errorCode =
                ProductApplicationErrorCode.PRODUCT_NOT_FOUND;

        return new ProductApplicationException(
                errorCode,
                "%s ID=%s".formatted(
                        errorCode.detail(),
                        productId
                ),
                Map.of("productId", productId)
        );
    }

    public static ProductApplicationException duplicateName(
            final String productName
    ) {
        return new ProductApplicationException(
                ProductApplicationErrorCode.DUPLICATE_PRODUCT_NAME,
                "이미 사용 중인 상품명입니다. name=%s"
                        .formatted(productName),
                Map.of("productName", productName)
        );
    }

    public static ProductApplicationException categoriesNotFound(final Set<String> categoryIds) {
        return new ProductApplicationException(
                ProductApplicationErrorCode.CATEGORY_NOT_FOUND,
                "일부 카테고리를 찾을 수 없습니다. existingCategoryIds=%s"
                        .formatted(categoryIds),
                Map.of("existingCategoryIds", Set.copyOf(categoryIds))
        );
    }

    public static ProductApplicationException categoriesNotActive(final Set<String> categoryIds) {
        return new ProductApplicationException(
                ProductApplicationErrorCode.CATEGORY_NOT_ACTIVE,
                ProductApplicationErrorCode.CATEGORY_NOT_ACTIVE.detail(),
                Map.of("categoryIds", Set.copyOf(categoryIds))
        );
    }

    private ProductApplicationException(final ProductApplicationErrorCode errorCode) {
        super(errorCode);
    }

    private ProductApplicationException(
            final ProductApplicationErrorCode errorCode,
            final String detail,
            final Map<String, ?> extensions
    ) {
        super(errorCode, detail, extensions);
    }

}
