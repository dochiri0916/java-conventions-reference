package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;

public record StreetAddress(String value) {

    public StreetAddress {
        if (value == null) {
            throw new ShippingAddressRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidShippingAddressException();
        }
    }
}
