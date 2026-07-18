package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;

import java.util.Map;

public final class ProductDomainException extends DomainException {

    // DESCRIPTION
    public static ProductDomainException descriptionRequired() {
        return new ProductDomainException(ProductDomainErrorCode.PRODUCT_DESCRIPTION_REQUIRED);
    }

    public static ProductDomainException descriptionTooShort(final int minimumLength) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_SHORT,
                "상품 설명은 최소 %d자 이상이어야 합니다."
                        .formatted(minimumLength),
                Map.of("minimumLength", minimumLength)
        );
    }

    public static ProductDomainException descriptionTooLong(final int maximumLength) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_DESCRIPTION_TOO_LONG,
                "상품 설명은 최대 %d자 이하이어야 합니다."
                        .formatted(maximumLength),
                Map.of("maximumLength", maximumLength)
        );
    }

    // MONEY
    public static ProductDomainException negativeMoney(final long amount) {
        return new ProductDomainException(
                ProductDomainErrorCode.MONEY_NEGATIVE,
                "금액은 0원 이상이어야 합니다. amount=%d"
                        .formatted(amount),
                Map.of("amount", amount)
        );
    }

    public static ProductDomainException moneyMultiplyNegativeQuantity(final int quantity) {
        return new ProductDomainException(
                ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE,
                "금액에 곱할 수량은 0개 이상이어야 합니다. quantity=%d"
                        .formatted(quantity),
                Map.of("quantity", quantity)
        );
    }

    public static ProductDomainException moneyOverflow() {
        return new ProductDomainException(ProductDomainErrorCode.MONEY_OVERFLOW);
    }

    // STOCK
    public static ProductDomainException negativeStock(final int stock) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_NEGATIVE,
                "재고는 0 이상이어야 합니다. stock=%d"
                        .formatted(stock),
                Map.of("stock", stock)
        );
    }

    public static ProductDomainException invalidStockQuantity(final int quantity) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_QUANTITY_INVALID,
                "재고 변경 수량은 1 이상이어야 합니다. quantity=%d"
                        .formatted(quantity),
                Map.of("quantity", quantity)
        );
    }

    public static ProductDomainException insufficientStock(
            final int currentStock,
            final int requestedQuantity
    ) {
        return new ProductDomainException(
                ProductDomainErrorCode.INSUFFICIENT_STOCK,
                "상품 재고가 부족합니다. currentStock=%d, requestedQuantity=%d"
                        .formatted(currentStock, requestedQuantity),
                Map.of(
                        "currentStock", currentStock,
                        "requestedQuantity", requestedQuantity
                )
        );
    }

    public static ProductDomainException stockOverflow(
            final int currentStock,
            final int requestedQuantity
    ) {
        return new ProductDomainException(
                ProductDomainErrorCode.STOCK_OVERFLOW,
                "재고가 허용 범위를 초과합니다. currentStock=%d, requestedQuantity=%d"
                        .formatted(currentStock, requestedQuantity),
                Map.of(
                        "currentStock", currentStock,
                        "requestedQuantity", requestedQuantity
                )
        );
    }

    // PRODUCT_STATUS
    public static ProductDomainException statusTransitionNotAllowed(
            final ProductStatus currentStatus,
            final ProductStatus targetStatus
    ) {
        return new ProductDomainException(
                ProductDomainErrorCode.PRODUCT_STATUS_TRANSITION_NOT_ALLOWED,
                "상품 상태를 %s에서 %s(으)로 변경할 수 없습니다."
                        .formatted(currentStatus, targetStatus),
                Map.of(
                        "currentStatus", currentStatus.name(),
                        "targetStatus", targetStatus.name()
                )
        );
    }

    private ProductDomainException(final ProductDomainErrorCode errorCode) {
        super(errorCode);
    }

    private ProductDomainException(
            final ProductDomainErrorCode errorCode,
            final String detail,
            final Map<String, ?> extensions
    ) {
        super(errorCode, detail, extensions);
    }

}
