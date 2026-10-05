package com.dochiri.tooltestlab.order.domain.exception;

public abstract sealed class OrderDomainException extends RuntimeException
        permits InvalidProductReferenceIdException,
        CartReferenceIdRequiredException,
        InvalidCartReferenceIdException,
        InvalidMemberReferenceIdException,
        InvalidOrderIdException,
        OrderIdRequiredException,
        QuantityRequiredException,
        InvalidShippingAddressException,
        ShippingAddressRequiredException,
        InvalidOrderLinesException,
        OrderLinesRequiredException,
        OrderStatusRequiredException,
        TotalAmountRequiredException,
        ProductReferenceIdRequiredException,
        ProductNameRequiredException,
        UnitPriceRequiredException,
        InvalidOrderStatusException,
        MemberReferenceIdRequiredException,
        NotOrderOwnerException {
    protected OrderDomainException(final String message) {
        super(message);
    }

}
