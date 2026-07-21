package com.dochiri.tooltestlab.catalog.product.adapter.in.web.exception;

import com.dochiri.tooltestlab.catalog.product.adapter.in.web.ProductWebAdapter;
import com.dochiri.tooltestlab.catalog.product.domain.exception.*;
import com.dochiri.tooltestlab.global.adapter.in.web.exception.ProblemDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(
        basePackageClasses = ProductWebAdapter.class
)
public final class ProductApiControllerAdvice {

    @ExceptionHandler(ProductIdRequiredException.class)
    public ProblemDetail handleProductIdRequired(final ProductIdRequiredException exception) {
        log.warn(
                "Product ID required: message={}",
                exception.getMessage()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_ID_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ProblemDetail handleInvalidProductId(final InvalidProductIdException exception) {
        log.warn(
                "Invalid product ID: productId={}",
                exception.productId()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "INVALID_PRODUCT_ID",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductNameRequiredException.class)
    public ProblemDetail handleProductNameRequired(final ProductNameRequiredException exception) {
        log.warn(
                "Product name required: message={}",
                exception.getMessage()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_NAME_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidProductNameException.class)
    public ProblemDetail handleInvalidProductName(final InvalidProductNameException exception) {
        log.warn(
                "Invalid product name: productName={}",
                exception.productName()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "INVALID_PRODUCT_NAME",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductDescriptionRequiredException.class)
    public ProblemDetail handleProductDescriptionRequired(final ProductDescriptionRequiredException exception) {
        log.warn(
                "Product description required: message={}",
                exception.getMessage()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_DESCRIPTION_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductDescriptionTooShortException.class)
    public ProblemDetail handleProductDescriptionTooShort(final ProductDescriptionTooShortException exception) {
        log.warn(
                "Product description too short: minLength={}",
                exception.minLength()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_DESCRIPTION_TOO_SHORT",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductDescriptionTooLongException.class)
    public ProblemDetail handleProductDescriptionTooLong(final ProductDescriptionTooLongException exception) {
        log.warn(
                "Product description too long: maxLength={}",
                exception.maxLength()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_DESCRIPTION_TOO_LONG",
                exception.getMessage()
        );
    }

    @ExceptionHandler(NegativeMoneyException.class)
    public ProblemDetail handleNegativeMoney(final NegativeMoneyException exception) {
        log.warn(
                "Money negative: amount={}",
                exception.amount()
        );

        return ProblemDetails.create(
                HttpStatus.BAD_REQUEST,
                "MONEY_NEGATIVE",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(final InsufficientStockException exception) {
        log.warn(
                "Insufficient product stock: currentStock={}, requestedQuantity={}",
                exception.currentStock(),
                exception.requestedQuantity()
        );

        return ProblemDetails.create(
                HttpStatus.CONFLICT,
                "PRODUCT_STOCK_INSUFFICIENT",
                exception.getMessage()
        );
    }

    @ExceptionHandler(StockOverflowException.class)
    public ProblemDetail handleStockOverflow(final StockOverflowException exception) {
        log.warn(
                "Product stock overflow: currentStock={}, requestedQuantity={}",
                exception.currentStock(),
                exception.requestedQuantity()
        );

        return ProblemDetails.create(
                HttpStatus.CONFLICT,
                "PRODUCT_STOCK_OVERFLOW",
                exception.getMessage()
        );
    }

    @ExceptionHandler(MoneyOverflowException.class)
    public ProblemDetail handleMoneyOverflow(final MoneyOverflowException exception) {
        log.warn(
                "Money overflow: message={}",
                exception.getMessage()
        );

        return ProblemDetails.create(
                HttpStatus.CONFLICT,
                "MONEY_OVERFLOW",
                exception.getMessage()
        );
    }

    @ExceptionHandler(NegativeMoneyMultiplierException.class)
    public ProblemDetail handleNegativeMoneyMultiplier(final NegativeMoneyMultiplierException exception) {
        log.warn(
                "Negative money multiplier: quantity={}",
                exception.quantity()
        );

        return ProblemDetails.create(
                HttpStatus.CONFLICT,
                "MONEY_MULTIPLIER_NEGATIVE",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidStockQuantityException.class)
    public ProblemDetail handleInvalidStockQuantity(final InvalidStockQuantityException exception) {
        log.warn(
                "Invalid stock quantity: currentQuantity={}",
                exception.quantity()
        );

        return ProblemDetails.create(
                HttpStatus.CONFLICT,
                "INVALID_STOCK_QUANTITY",
                exception.getMessage()
        );
    }

    @ExceptionHandler(StatusTransitionNotAllowedException.class)
    public ProblemDetail handleStatusTransitionNotAllowed(final StatusTransitionNotAllowedException exception) {
        log.warn(
                "Status transition not allowed: currentStatus={}, targetStatus={}",
                exception.currentStatus(),
                exception.targetStatus()
        );

        return ProblemDetails.create(
                HttpStatus.CONFLICT,
                "STATUS_TRANSITION_NOT_ALLOWED",
                exception.getMessage()
        );
    }

}
