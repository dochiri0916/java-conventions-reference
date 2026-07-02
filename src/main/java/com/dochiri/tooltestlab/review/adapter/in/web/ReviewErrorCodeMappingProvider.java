package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReviewErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                Map.entry("INVALID_REVIEW_ID", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("REVIEW_ID_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_RATING", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("RATING_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("BAD_RVW_CONTENT", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("REVIEW_CONTENT_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_REVIEW", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("REVIEW_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("REVIEW_NOT_FOUND", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND)),
                Map.entry("DUPLICATE_REVIEW", new ApiExceptionMapper.Mapping(HttpStatus.CONFLICT, ProblemType.CONFLICT)),
                Map.entry("NOT_REVIEW_OWNER", new ApiExceptionMapper.Mapping(HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN)),
                Map.entry("INVALID_INPUT", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED))
        );
    }
}
