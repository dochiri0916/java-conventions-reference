package com.dochiri.tooltestlab.product.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.product.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.product.application.exception.ProductErrorCode;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidMaxCountException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidStockException;
import com.dochiri.tooltestlab.product.domain.exception.ProductDomainErrorCode;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ProductErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                validation(ProductDomainErrorCode.BAD_PRODUCT_ID),
                validation(ProductDomainErrorCode.PRODUCT_ID_REQUIRED),
                validation(ProductDomainErrorCode.BAD_PRODUCT_NAME),
                validation(ProductDomainErrorCode.PRODUCT_NAME_REQUIRED),
                validation(ProductDomainErrorCode.INVALID_STOCK),
                validation(ProductDomainErrorCode.STOCK_REQUIRED),
                validation(ProductDomainErrorCode.INVALID_PRODUCT),
                validation(ProductDomainErrorCode.PRODUCT_REQUIRED),
                validation(ProductDomainErrorCode.MONEY_NEGATIVE),
                validation(ProductDomainErrorCode.MONEY_ADD_NULL),
                validation(ProductDomainErrorCode.MONEY_REQUIRED),
                validation(ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE),
                validation(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE),
                businessConflict(ProductDomainErrorCode.LOW_STOCK),
                notFound(ProductErrorCode.PRODUCT_NOT_FOUND),
                validation(ProductErrorCode.INVALID_INPUT)
        );
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof InvalidProductIdException invalidProductIdException) {
            return resolve(invalidProductIdException.code());
        }
        if (exception instanceof InvalidProductNameException invalidProductNameException) {
            return resolve(invalidProductNameException.code());
        }
        if (exception instanceof InvalidStockException invalidStockException) {
            return resolve(invalidStockException.code());
        }
        if (exception instanceof InvalidProductException invalidProductException) {
            return resolve(invalidProductException.code());
        }
        if (exception instanceof InvalidMoneyException invalidMoneyException) {
            return resolve(invalidMoneyException.code());
        }
        if (exception instanceof InvalidMaxCountException invalidMaxCountException) {
            return resolve(invalidMaxCountException.code());
        }
        if (exception instanceof InsufficientStockException insufficientStockException) {
            return resolve(insufficientStockException.code());
        }
        if (exception instanceof ProductNotFoundException productNotFoundException) {
            return resolve(productNotFoundException.code());
        }
        if (exception instanceof InvalidCommandException invalidCommandException) {
            return resolve(invalidCommandException.code());
        }
        return Optional.empty();
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> validation(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> businessConflict(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.CONFLICT, ProblemType.BUSINESS_RULE_VIOLATION);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> notFound(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> mapping(
            final Enum<?> errorCode,
            final HttpStatus status,
            final ProblemType problemType
    ) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiExceptionMapper.Mapping(status, problemType));
    }
}
