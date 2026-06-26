package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.global.error.BusinessException;

public abstract class OrderDomainException extends BusinessException {

    protected OrderDomainException(OrderDomainErrorCode errorCode) {
        super(errorCode);
    }

    protected OrderDomainException(OrderDomainErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public OrderDomainErrorCode errorCode() {
        return (OrderDomainErrorCode) super.errorCode();
    }

}
