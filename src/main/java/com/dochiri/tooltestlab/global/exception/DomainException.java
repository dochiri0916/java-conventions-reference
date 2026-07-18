package com.dochiri.tooltestlab.global.exception;

import java.util.Map;

public abstract class DomainException extends BusinessException {

    protected DomainException(final ErrorCode errorCode) {
        super(errorCode);
    }

    protected DomainException(
            final ErrorCode errorCode,
            final String detail,
            final Map<String, ?> extensions
    ) {
        super(errorCode, detail, extensions);
    }

}
