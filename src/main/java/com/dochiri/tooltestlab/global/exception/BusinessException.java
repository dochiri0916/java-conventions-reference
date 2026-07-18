package com.dochiri.tooltestlab.global.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> extensions;

    protected BusinessException(final ErrorCode errorCode) {
        this(
                errorCode,
                errorCode.detail(),
                Map.of()
        );
    }

    protected BusinessException(
            final ErrorCode errorCode,
            final String detail,
            final Map<String, ?> extensions
    ) {
        super(requireNonNull(detail));
        this.errorCode = requireNonNull(errorCode);
        this.extensions = copyOf(extensions);
    }

    public ErrorCode errorCode() {
        return errorCode;
    }

    public Map<String, Object> extensions() {
        return extensions;
    }

    private static Map<String, Object> copyOf(
            final Map<String, ?> source
    ) {
        final Map<String, Object> copied = new LinkedHashMap<>(source);

        return Collections.unmodifiableMap(copied);
    }

}
