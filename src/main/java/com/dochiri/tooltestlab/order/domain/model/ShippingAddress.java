package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

public record ShippingAddress(RecipientName recipientName, Phone phone, StreetAddress address, ZipCode zipCode) {

    public ShippingAddress {
        if (recipientName == null || phone == null || address == null || zipCode == null) {
            throw OrderDomainException.shippingAddressRequired();
        }
    }
}
