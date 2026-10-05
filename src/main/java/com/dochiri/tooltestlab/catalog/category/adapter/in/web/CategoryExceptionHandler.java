package com.dochiri.tooltestlab.catalog.category.adapter.in.web;

import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryApplicationException;
import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryDomainException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryIdRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryNameRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryIdException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryStatusRequiredException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryNameException;
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
public final class CategoryExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(CategoryDomainException.class)
    public ProblemDetail handle(final CategoryDomainException exception) {
        return switch (exception) {
            case CategoryIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "CATEGORY_ID_REQUIRED");
            case InvalidCategoryIdException e -> respond(e, HttpStatus.BAD_REQUEST, "CATEGORY_ID_INVALID");
            case CategoryNameRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "CATEGORY_NAME_REQUIRED");
            case InvalidCategoryNameException e -> respond(e, HttpStatus.BAD_REQUEST, "CATEGORY_NAME_INVALID");
            case CategoryStatusRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "CATEGORY_STATUS_REQUIRED");
        };
    }

    @ExceptionHandler(CategoryApplicationException.class)
    public ProblemDetail handle(final CategoryApplicationException exception) {
        return switch (exception) {
            case DuplicateCategoryNameException e -> respond(e, HttpStatus.CONFLICT, "DUPLICATE_CATEGORY_NAME");
            case InvalidCommandException e -> respond(e, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
        };
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("Category 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass()
                        .getSimpleName(), code,
                exception.getMessage());
        return problemDetails.create(status, code,
                exception.getMessage());
    }

}
