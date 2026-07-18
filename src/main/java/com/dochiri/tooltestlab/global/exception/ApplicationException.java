package com.dochiri.tooltestlab.global.exception;

import java.util.Map;

public abstract class ApplicationException extends BusinessException {

    protected ApplicationException(final ErrorCode errorCode) {
        super(errorCode);
    }

    protected ApplicationException(
            final ErrorCode errorCode,
            final String detail,
            final Map<String, ?> extensions
    ) {
        super(errorCode, detail, extensions);
    }

}