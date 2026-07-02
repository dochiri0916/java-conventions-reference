package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CartErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                Map.entry("INVALID_CART_ID", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("CART_ID_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_CART_ITEM", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("CART_ITEM_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_CART", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("CART_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("TOO_MANY_ITEMS", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("CART_NOT_FOUND", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND)),
                Map.entry("CART_ITEM_NOT_FOUND", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND)),
                Map.entry("NOT_CART_OWNER", new ApiExceptionMapper.Mapping(HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN)),
                Map.entry("INVALID_INPUT", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED))
        );
    }
}
