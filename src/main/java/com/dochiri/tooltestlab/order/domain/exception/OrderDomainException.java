package com.dochiri.tooltestlab.order.domain.exception;

public abstract sealed class OrderDomainException extends RuntimeException
        permits InvalidOrderIdException,
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
        MemberReferenceIdRequiredException {
    protected OrderDomainException(final String message) {
        super(message);
    }

}
