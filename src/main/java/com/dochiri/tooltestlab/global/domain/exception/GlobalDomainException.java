package com.dochiri.tooltestlab.global.domain.exception;

public abstract sealed class GlobalDomainException extends RuntimeException
        permits InvalidQuantityException,
        MoneyOverflowException,
        MoneyRequiredException,
        InvalidOutboxSequenceException,
        InvalidOutboxAttemptsException,
        NegativeMoneyException,
        NegativeMoneyMultiplierException {

    protected GlobalDomainException(final String message) {
        super(message);
    }
}
