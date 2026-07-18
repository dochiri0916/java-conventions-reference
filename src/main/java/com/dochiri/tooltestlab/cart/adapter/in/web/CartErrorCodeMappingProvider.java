package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.application.exception.CartErrorCode;
import com.dochiri.tooltestlab.cart.application.exception.CartNotFoundException;
import com.dochiri.tooltestlab.cart.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.cart.domain.exception.*;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class CartErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                validation(CartDomainErrorCode.INVALID_CART_ID),
                validation(CartDomainErrorCode.CART_ID_REQUIRED),
                validation(CartDomainErrorCode.INVALID_CART_ITEM),
                validation(CartDomainErrorCode.CART_ITEM_REQUIRED),
                validation(CartDomainErrorCode.INVALID_CART),
                validation(CartDomainErrorCode.CART_REQUIRED),
                validation(CartDomainErrorCode.TOO_MANY_ITEMS),
                notFound(CartErrorCode.CART_NOT_FOUND),
                notFound(CartErrorCode.CART_ITEM_NOT_FOUND),
                forbidden(CartErrorCode.NOT_CART_OWNER),
                validation(CartErrorCode.INVALID_INPUT)
        );
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof InvalidCartIdException invalidCartIdException) {
            return resolve(invalidCartIdException.code());
        }
        if (exception instanceof InvalidCartItemException invalidCartItemException) {
            return resolve(invalidCartItemException.code());
        }
        if (exception instanceof InvalidCartException invalidCartException) {
            return resolve(invalidCartException.code());
        }
        if (exception instanceof CartItemLimitExceededException cartItemLimitExceededException) {
            return resolve(cartItemLimitExceededException.code());
        }
        if (exception instanceof CartNotFoundException cartNotFoundException) {
            return resolve(cartNotFoundException.code());
        }
        if (exception instanceof InvalidCommandException invalidCommandException) {
            return resolve(invalidCommandException.code());
        }
        return Optional.empty();
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> validation(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);
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
