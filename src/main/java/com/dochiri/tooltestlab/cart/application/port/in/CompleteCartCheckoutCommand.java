package com.dochiri.tooltestlab.cart.application.port.in;

public record CompleteCartCheckoutCommand(String memberId, String cartId, long cartRevision) {
}
