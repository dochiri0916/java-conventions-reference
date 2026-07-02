package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                Map.entry("INVALID_ORDER_ID", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("ORDER_ID_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_QUANTITY", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("QUANTITY_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("BAD_SHIPPING_ADDR", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("SHIPPING_ADDRESS_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("BAD_ORDER_LINES", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("ORDER_LINES_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("ORDER_STATUS_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("TOTAL_AMOUNT_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("PRODUCT_ID_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("PRODUCT_NAME_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("UNIT_PRICE_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("BAD_ORDER_STATUS", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.BUSINESS_RULE_VIOLATION)),
                Map.entry("EMPTY_CART", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.BUSINESS_RULE_VIOLATION)),
                Map.entry("ORDER_NOT_FOUND", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND)),
                Map.entry("NOT_ORDER_OWNER", new ApiExceptionMapper.Mapping(HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN))
        );
    }
}
