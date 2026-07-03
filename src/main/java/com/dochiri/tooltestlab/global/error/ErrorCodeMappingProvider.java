package com.dochiri.tooltestlab.global.error;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@FunctionalInterface
public interface ErrorCodeMappingProvider {
    Map<String, ApiExceptionMapper.Mapping> errorCodeMappings();

    default Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        Objects.requireNonNull(exception, "exception");
        return Optional.empty();
    }

    default Optional<ApiExceptionMapper.MappedError> resolve(final Enum<?> errorCode) {
        final String code = ApiErrorCode.from(Objects.requireNonNull(errorCode, "errorCode"));
        final ApiExceptionMapper.Mapping mapping = errorCodeMappings().get(code);
        if (mapping == null) {
            return Optional.empty();
        }
        return Optional.of(new ApiExceptionMapper.MappedError(code, mapping));
    }
}
