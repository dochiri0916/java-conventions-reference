package com.dochiri.tooltestlab.global.error;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@FunctionalInterface
public interface ApiErrorMessageProvider {

    Map<String, ApiErrorMessage> errorMessages();

    default Optional<ApiErrorMessage> findByCode(final String errorCode) {
        final String code = Objects.requireNonNull(errorCode, "errorCode");
        return Optional.ofNullable(errorMessages().get(code));
    }
}
