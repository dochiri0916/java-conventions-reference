package com.dochiri.tooltestlab.catalog.product.adapter.in.web;

import com.dochiri.tooltestlab.catalog.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidCategoryIdsException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidProductNameException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidStockQuantityException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionTooLongException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionTooShortException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductIdRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductPriceRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductStockRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductStatusRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductCategoryIdsRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.StatusTransitionNotAllowedException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.StockOverflowException;
import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(
        assignableTypes = ProductController.class
)
public final class ProductDomainExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(ProductIdRequiredException.class)
    public ProblemDetail handleProductIdRequired(final ProductIdRequiredException exception) {
        log.warn(
                "Product ID required: message={}",
                exception.getMessage()
        );

        return problemDetails.create(
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

        return problemDetails.create(
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

        return problemDetails.create(
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

        return problemDetails.create(
                HttpStatus.BAD_REQUEST,
                "INVALID_PRODUCT_NAME",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductPriceRequiredException.class)
    public ProblemDetail handleProductPriceRequired(final ProductPriceRequiredException exception) {
        log.warn(
                "Product price required: message={}",
                exception.getMessage()
        );

        return problemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_PRICE_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductStockRequiredException.class)
    public ProblemDetail handleProductStockRequired(final ProductStockRequiredException exception) {
        log.warn(
                "Product stock required: message={}",
                exception.getMessage()
        );

        return problemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_STOCK_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductStatusRequiredException.class)
    public ProblemDetail handleProductStatusRequired(final ProductStatusRequiredException exception) {
        log.warn(
                "Product status required: message={}",
                exception.getMessage()
        );

        return problemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_STATUS_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ProductCategoryIdsRequiredException.class)
    public ProblemDetail handleProductCategoryIdsRequired(final ProductCategoryIdsRequiredException exception) {
        log.warn(
                "Product category ids required: message={}",
                exception.getMessage()
        );

        return problemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_CATEGORY_IDS_REQUIRED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidCategoryIdsException.class)
    public ProblemDetail handleInvalidCategoryIds(final InvalidCategoryIdsException exception) {
        log.warn("Invalid product category ids: message={}", exception.getMessage());
        return problemDetails.create(HttpStatus.BAD_REQUEST, "PRODUCT_CATEGORY_IDS_INVALID", exception.getMessage());
    }

    @ExceptionHandler(ProductDescriptionRequiredException.class)
    public ProblemDetail handleProductDescriptionRequired(final ProductDescriptionRequiredException exception) {
        log.warn(
                "Product description required: message={}",
                exception.getMessage()
        );

        return problemDetails.create(
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

        return problemDetails.create(
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

        return problemDetails.create(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_DESCRIPTION_TOO_LONG",
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

        return problemDetails.create(
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

        return problemDetails.create(
                HttpStatus.CONFLICT,
                "PRODUCT_STOCK_OVERFLOW",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidStockQuantityException.class)
    public ProblemDetail handleInvalidStockQuantity(final InvalidStockQuantityException exception) {
        log.warn(
                "Invalid stock quantity: currentQuantity={}",
                exception.quantity()
        );

        return problemDetails.create(
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

        return problemDetails.create(
                HttpStatus.CONFLICT,
                "STATUS_TRANSITION_NOT_ALLOWED",
                exception.getMessage()
        );
    }

}
