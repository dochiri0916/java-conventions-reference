package com.dochiri.tooltestlab.cart.application.port.in.command;

public record CompleteCartCheckoutCommand(String memberId, String cartId, long cartRevision) {
}
