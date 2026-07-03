package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderApplicationErrorCode;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainErrorCode;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class OrderErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                validation(OrderDomainErrorCode.INVALID_ORDER_ID),
                validation(OrderDomainErrorCode.ORDER_ID_REQUIRED),
                validation(OrderDomainErrorCode.INVALID_QUANTITY),
                validation(OrderDomainErrorCode.QUANTITY_REQUIRED),
                validation(OrderDomainErrorCode.BAD_SHIPPING_ADDR),
                validation(OrderDomainErrorCode.SHIPPING_ADDRESS_REQUIRED),
                validation(OrderDomainErrorCode.BAD_ORDER_LINES),
                validation(OrderDomainErrorCode.ORDER_LINES_REQUIRED),
                validation(OrderDomainErrorCode.ORDER_STATUS_REQUIRED),
                validation(OrderDomainErrorCode.TOTAL_AMOUNT_REQUIRED),
                validation(OrderDomainErrorCode.PRODUCT_ID_REQUIRED),
                validation(OrderDomainErrorCode.PRODUCT_NAME_REQUIRED),
                validation(OrderDomainErrorCode.UNIT_PRICE_REQUIRED),
                businessBadRequest(OrderDomainErrorCode.BAD_ORDER_STATUS),
                businessBadRequest(OrderApplicationErrorCode.EMPTY_CART),
                notFound(OrderApplicationErrorCode.ORDER_NOT_FOUND),
                forbidden(OrderApplicationErrorCode.NOT_ORDER_OWNER)
        );
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof InvalidOrderIdException invalidOrderIdException) {
            return resolve(invalidOrderIdException.code());
        }
        if (exception instanceof InvalidQuantityException invalidQuantityException) {
            return resolve(invalidQuantityException.code());
        }
        if (exception instanceof InvalidShippingAddressException invalidShippingAddressException) {
            return resolve(invalidShippingAddressException.code());
        }
        if (exception instanceof InvalidOrderLinesException invalidOrderLinesException) {
            return resolve(invalidOrderLinesException.code());
        }
        if (exception instanceof InvalidOrderStatusException invalidOrderStatusException) {
            return resolve(invalidOrderStatusException.code());
        }
        if (exception instanceof EmptyCartException emptyCartException) {
            return resolve(emptyCartException.code());
        }
        if (exception instanceof OrderNotFoundException orderNotFoundException) {
            return resolve(orderNotFoundException.code());
        }
        if (exception instanceof NotOrderOwnerException notOrderOwnerException) {
            return resolve(notOrderOwnerException.code());
        }
        return Optional.empty();
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> validation(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> businessBadRequest(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.BAD_REQUEST, ProblemType.BUSINESS_RULE_VIOLATION);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> notFound(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> forbidden(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> mapping(
            final Enum<?> errorCode,
            final HttpStatus status,
            final ProblemType problemType
    ) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiExceptionMapper.Mapping(status, problemType));
    }
}
