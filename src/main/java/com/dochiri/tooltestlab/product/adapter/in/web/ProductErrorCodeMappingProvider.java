package com.dochiri.tooltestlab.product.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                Map.entry("BAD_PRODUCT_ID", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("PRODUCT_ID_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("BAD_PRODUCT_NAME", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("PRODUCT_NAME_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_STOCK", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("STOCK_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_PRODUCT", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("PRODUCT_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MONEY_NEGATIVE", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MONEY_ADD_NULL", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MONEY_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MONEY_MULTIPLY_NEGATIVE", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MAX_COUNT_NOT_POSITIVE", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("LOW_STOCK", new ApiExceptionMapper.Mapping(HttpStatus.CONFLICT, ProblemType.BUSINESS_RULE_VIOLATION)),
                Map.entry("PRODUCT_NOT_FOUND", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND)),
                Map.entry("INVALID_INPUT", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED))
        );
    }
}
