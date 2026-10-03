package com.dochiri.tooltestlab.order.application.exception;

public abstract sealed class OrderApplicationException extends RuntimeException
        permits OrderNotFoundException,
        NotOrderOwnerException,
        EmptyCartException {
    protected OrderApplicationException(final String message) {
        super(message);
    }

}
