package com.dochiri.tooltestlab.order.application.exception;

import com.dochiri.tooltestlab.global.error.BusinessException;
import com.dochiri.tooltestlab.global.error.ErrorCode;

public abstract class OrderApplicationException extends BusinessException {

    protected OrderApplicationException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected OrderApplicationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public OrderApplicationErrorCode errorCode() {
        return (OrderApplicationErrorCode) super.errorCode();
    }

}
