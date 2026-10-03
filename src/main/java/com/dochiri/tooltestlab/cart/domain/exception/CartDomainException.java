package com.dochiri.tooltestlab.cart.domain.exception;

public abstract sealed class CartDomainException extends RuntimeException
        permits CartIdRequiredException,
        InvalidCartIdException,
        CartItemRequiredException,
        InvalidCartItemException,
        CartRequiredException,
        InvalidCartException,
        TooManyCartItemsException,
        ProductReferenceIdRequiredException,
        ProductNameRequiredException,
        QuantityRequiredException,
        UnitPriceRequiredException,
        MemberReferenceIdRequiredException {
    protected CartDomainException(final String message) {
        super(message);
    }

}
