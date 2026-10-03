package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;

public record Phone(String value) {

    public Phone {
        if (value == null) {
            throw new ShippingAddressRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidShippingAddressException();
        }
    }
}
