package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.order.domain.exception.InvalidProductReferenceIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidMemberReferenceIdException;
import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderApplicationException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.order.domain.exception.MemberReferenceIdRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;
import com.dochiri.tooltestlab.order.domain.exception.OrderIdRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.OrderLinesRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.OrderStatusRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.TotalAmountRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.UnitPriceRequiredException;
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
public final class OrderExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(OrderDomainException.class)
    public ProblemDetail handle(final OrderDomainException exception) {
        return switch (exception) {
            case InvalidProductReferenceIdException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_ID_INVALID");
            case InvalidMemberReferenceIdException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_INVALID");
            case InvalidOrderIdException e -> respond(e, HttpStatus.BAD_REQUEST, "ORDER_ID_INVALID");
            case OrderIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "ORDER_ID_REQUIRED");
            case QuantityRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "QUANTITY_REQUIRED");
            case InvalidShippingAddressException e -> respond(e, HttpStatus.BAD_REQUEST, "SHIPPING_ADDRESS_INVALID");
            case ShippingAddressRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "SHIPPING_ADDRESS_REQUIRED");
            case InvalidOrderLinesException e -> respond(e, HttpStatus.BAD_REQUEST, "ORDER_LINES_INVALID");
            case OrderLinesRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "ORDER_LINES_REQUIRED");
            case OrderStatusRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "ORDER_STATUS_REQUIRED");
            case TotalAmountRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "TOTAL_AMOUNT_REQUIRED");
            case ProductReferenceIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_ID_REQUIRED");
            case MemberReferenceIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_REQUIRED");
            case ProductNameRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_NAME_REQUIRED");
            case UnitPriceRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "UNIT_PRICE_REQUIRED");
            case InvalidOrderStatusException e -> respond(e, HttpStatus.CONFLICT, "BAD_ORDER_STATUS");
        };
    }

    @ExceptionHandler(OrderApplicationException.class)
    public ProblemDetail handle(final OrderApplicationException exception) {
        return switch (exception) {
            case OrderNotFoundException e -> respond(e, HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND");
            case NotOrderOwnerException e -> respond(e, HttpStatus.FORBIDDEN, "NOT_ORDER_OWNER");
            case EmptyCartException e -> respond(e, HttpStatus.CONFLICT, "EMPTY_CART");
        };
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("Order 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass().getSimpleName(), code, exception.getMessage());
        return problemDetails.create(status, code, exception.getMessage());
    }

}
