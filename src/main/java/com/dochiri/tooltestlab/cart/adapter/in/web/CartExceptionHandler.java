package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidProductReferenceIdException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidMemberReferenceIdException;
import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import com.dochiri.tooltestlab.cart.application.exception.CartApplicationException;
import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.exception.CartCreationConflictException;
import com.dochiri.tooltestlab.cart.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.cart.application.exception.ProductSnapshotNotReadyException;
import com.dochiri.tooltestlab.cart.domain.exception.CartDomainException;
import com.dochiri.tooltestlab.cart.domain.exception.CartIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.CartItemRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.CartRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.exception.MemberReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.TooManyCartItemsException;
import com.dochiri.tooltestlab.cart.domain.exception.UnitPriceRequiredException;
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
public final class CartExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(CartDomainException.class)
    public ProblemDetail handle(final CartDomainException exception) {
        return switch (exception) {
            case InvalidProductReferenceIdException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_ID_INVALID");
            case InvalidMemberReferenceIdException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_INVALID");
            case CartIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "CART_ID_REQUIRED");
            case InvalidCartIdException e -> respond(e, HttpStatus.BAD_REQUEST, "CART_ID_INVALID");
            case CartItemRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "CART_ITEM_REQUIRED");
            case InvalidCartItemException e -> respond(e, HttpStatus.BAD_REQUEST, "CART_ITEM_INVALID");
            case CartRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "CART_REQUIRED");
            case InvalidCartException e -> respond(e, HttpStatus.BAD_REQUEST, "CART_INVALID");
            case TooManyCartItemsException e -> respond(e, HttpStatus.BAD_REQUEST, "TOO_MANY_ITEMS");
            case ProductReferenceIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_ID_REQUIRED");
            case MemberReferenceIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_REQUIRED");
            case ProductNameRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_NAME_REQUIRED");
            case QuantityRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "QUANTITY_REQUIRED");
            case UnitPriceRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "UNIT_PRICE_REQUIRED");
        };
    }

    @ExceptionHandler(CartApplicationException.class)
    public ProblemDetail handle(final CartApplicationException exception) {
        return switch (exception) {
            case CartNotFoundException e -> respond(e, HttpStatus.NOT_FOUND, "CART_NOT_FOUND");
            case CartCreationConflictException e -> respond(e, HttpStatus.CONFLICT, "CART_CREATION_CONFLICT");
            case ProductSnapshotNotReadyException e -> respond(e, HttpStatus.CONFLICT, "PRODUCT_SNAPSHOT_NOT_READY");
            case InvalidCommandException e -> respond(e, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
        };
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("Cart 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass().getSimpleName(), code, exception.getMessage());
        return problemDetails.create(status, code, exception.getMessage());
    }

}
