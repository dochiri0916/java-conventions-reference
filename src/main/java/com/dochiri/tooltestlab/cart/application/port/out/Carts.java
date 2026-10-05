package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.domain.Cart;

import java.util.List;

public record Carts(List<Cart> values) {

    public Carts {
        values = List.copyOf(values);
    }

}
