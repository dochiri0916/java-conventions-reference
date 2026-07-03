package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GlobalErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    private static final Map<String, ApiExceptionMapper.Mapping> ERROR_CODE_MAPPINGS = Map.of(
            ApiErrorCode.from(GlobalErrorCode.AUTHENTICATION_REQUIRED),
            new ApiExceptionMapper.Mapping(HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED)
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
