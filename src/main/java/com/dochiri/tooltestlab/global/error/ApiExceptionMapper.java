package com.dochiri.tooltestlab.global.error;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiExceptionMapper {

    private final List<ErrorCodeMappingProvider> providers;

    public record Mapping(HttpStatus status, ProblemType problemType) {
    }

    public Mapping map(final String errorCode) {
        for (final var provider : providers) {
            final var mapping = provider.errorCodeMappings().get(errorCode);
            if (mapping != null) {
                return mapping;
            }
        }
        return new Mapping(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.INTERNAL_ERROR);
    }
}
