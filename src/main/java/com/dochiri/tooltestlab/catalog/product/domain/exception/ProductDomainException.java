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

    public static ProductDomainException productIdRequired() {
        return new ProductDomainException(ProductDomainErrorCode.PRODUCT_ID_REQUIRED, Map.of());
    }

    public static ProductDomainException invalidProductId(final String productId) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_ID_INVALID,
                Map.of("productId", String.valueOf(productId))
        );
    }

    public static ProductDomainException productNameRequired() {
        return new ProductDomainException(ProductDomainErrorCode.PRODUCT_NAME_REQUIRED, Map.of());
    }

    public static ProductDomainException invalidProductName(final String productName) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_NAME_INVALID,
                Map.of("productName", String.valueOf(productName))
        );
    }

    public static ProductDomainException productDescriptionRequired() {
        return new ProductDomainException(ProductDomainErrorCode.PRODUCT_DESCRIPTION_REQUIRED, Map.of());
    }

    public static ProductDomainException productDescriptionTooShort(final int minimumLength) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_SHORT,
                Map.of("minimumLength", minimumLength)
        );
    }

    public static ProductDomainException productDescriptionTooLong(final int maximumLength) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_LONG,
                Map.of("maximumLength", maximumLength)
        );
    }

    public static ProductDomainException negativeMoney(final long amount) {
        return new ProductDomainException(ProductDomainErrorCode.MONEY_NEGATIVE, Map.of("amount", amount));
    }

    public static ProductDomainException negativeMoneyMultiplier(final int quantity) {
        return new ProductDomainException(
                ProductDomainErrorCode.MONEY_MULTIPLIER_NEGATIVE,
                Map.of("quantity", quantity)
        );
    }

    public static ProductDomainException moneyOverflow() {
        return new ProductDomainException(ProductDomainErrorCode.MONEY_OVERFLOW, Map.of());
    }

    public static ProductDomainException negativeStock(final int stock) {
        return new ProductDomainException(ProductDomainErrorCode.STOCK_NEGATIVE, Map.of("stock", stock));
    }

    public static ProductDomainException invalidStockQuantity(final int quantity) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_QUANTITY_INVALID,
                Map.of("quantity", quantity)
        );
    }

    public static ProductDomainException insufficientStock(
            final int currentStock,
            final int requestedQuantity
    ) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_INSUFFICIENT,
                Map.of("currentStock", currentStock, "requestedQuantity", requestedQuantity)
        );
    }

    public static ProductDomainException stockOverflow(
            final int currentStock,
            final int requestedQuantity
    ) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_OVERFLOW,
                Map.of("currentStock", currentStock, "requestedQuantity", requestedQuantity)
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
