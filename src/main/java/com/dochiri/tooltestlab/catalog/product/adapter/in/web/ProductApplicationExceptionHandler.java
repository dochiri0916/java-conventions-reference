package com.dochiri.tooltestlab.catalog.product.adapter.in.web;

import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import com.dochiri.tooltestlab.catalog.product.application.exception.CategoriesNotActiveException;
import com.dochiri.tooltestlab.catalog.product.application.exception.CategoriesNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.exception.DuplicateProductNameException;
import com.dochiri.tooltestlab.catalog.product.application.exception.InsufficientProductStockException;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationException;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductStockOverflowException;
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
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class ProductApplicationExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(ProductApplicationException.class)
    public ProblemDetail handle(final ProductApplicationException exception) {
        return switch (exception) {
            case ProductNotFoundException e -> respond(e, HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND");
            case DuplicateProductNameException e -> respond(e, HttpStatus.CONFLICT, "DUPLICATE_PRODUCT_NAME");
            case CategoriesNotFoundException e -> respond(e, HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND");
            case CategoriesNotActiveException e -> respond(e, HttpStatus.CONFLICT, "CATEGORY_NOT_ACTIVE");
            case InsufficientProductStockException e -> respond(e, HttpStatus.CONFLICT, "STOCK_INSUFFICIENT");
            case ProductStockOverflowException e -> respond(e, HttpStatus.CONFLICT, "STOCK_OVERFLOW");
        };
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("Product 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass()
                        .getSimpleName(), code,
                exception.getMessage());
        return problemDetails.create(status, code,
                exception.getMessage());
    }

}
