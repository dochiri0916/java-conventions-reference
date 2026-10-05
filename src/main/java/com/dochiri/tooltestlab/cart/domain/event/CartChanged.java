package com.dochiri.tooltestlab.cart.domain.event;

import com.dochiri.tooltestlab.cart.domain.exception.CartRequiredException;
import com.dochiri.tooltestlab.cart.domain.Cart;

public record CartChanged(Cart cart) {

    public CartChanged {
        if (cart == null) {
            throw new CartRequiredException();
        }
    }

}
