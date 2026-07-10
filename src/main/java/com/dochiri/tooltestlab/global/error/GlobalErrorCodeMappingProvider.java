package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GlobalErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    private static final Map<String, ApiExceptionMapper.Mapping> ERROR_CODE_MAPPINGS = Map.of(
            ApiErrorCode.from(GlobalErrorCode.INTERNAL_SERVER_ERROR),
            new ApiExceptionMapper.Mapping(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.INTERNAL_ERROR),
            ApiErrorCode.from(GlobalErrorCode.VALIDATION_ERROR),
            new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED),
            ApiErrorCode.from(GlobalErrorCode.AUTHENTICATION_REQUIRED),
            new ApiExceptionMapper.Mapping(HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED),
            ApiErrorCode.from(GlobalErrorCode.MALFORMED_REQUEST_BODY),
            new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED),
            ApiErrorCode.from(GlobalErrorCode.PARAMETER_TYPE_MISMATCH),
            new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED),
            ApiErrorCode.from(GlobalErrorCode.MISSING_PARAMETER),
            new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED),
            ApiErrorCode.from(GlobalErrorCode.METHOD_NOT_ALLOWED),
            new ApiExceptionMapper.Mapping(HttpStatus.METHOD_NOT_ALLOWED, ProblemType.METHOD_NOT_ALLOWED),
            ApiErrorCode.from(GlobalErrorCode.ROUTE_NOT_FOUND),
            new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND),
            ApiErrorCode.from(GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE),
            new ApiExceptionMapper.Mapping(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ProblemType.UNSUPPORTED_MEDIA_TYPE),
            ApiErrorCode.from(GlobalErrorCode.NOT_ACCEPTABLE),
            new ApiExceptionMapper.Mapping(HttpStatus.NOT_ACCEPTABLE, ProblemType.NOT_ACCEPTABLE)
    );

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.copyOf(ERROR_CODE_MAPPINGS);
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof AuthenticationRequiredException authenticationRequiredException) {
            return resolve(authenticationRequiredException.code());
        }
        return Optional.empty();
    }
}
