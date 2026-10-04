package com.dochiri.tooltestlab.cart.application.exception;

public abstract sealed class CartApplicationException extends RuntimeException
        permits CartNotFoundException,
        ProductSnapshotNotReadyException,
        InvalidCommandException {
    protected CartApplicationException(final String message) {
        super(message);
    }

}
