package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;
import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class ProductDomainException extends DomainException {

    private ProductDomainException(
            final ProductDomainErrorCode errorCode,
            final Map<String, ?> extensions
    ) {
        super(errorCode, errorCode.detail(), extensions);
    }

    public static ProductDomainException invalidStockQuantity(final int quantity) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_QUANTITY_INVALID,
                Map.of("quantity", quantity)
        );
    }

    public static ProductDomainException statusTransitionNotAllowed(
            final ProductStatus currentStatus,
            final ProductStatus targetStatus
    ) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_STATUS_TRANSITION_NOT_ALLOWED,
                Map.of("currentStatus", currentStatus.name(), "targetStatus", targetStatus.name())
        );
    }
}
