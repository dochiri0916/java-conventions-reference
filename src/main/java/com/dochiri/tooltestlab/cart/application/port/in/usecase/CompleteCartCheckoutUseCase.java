package com.dochiri.tooltestlab.cart.application.port.in.usecase;

import com.dochiri.tooltestlab.cart.application.port.in.command.CompleteCartCheckoutCommand;

@FunctionalInterface
public interface CompleteCartCheckoutUseCase {
    void complete(CompleteCartCheckoutCommand command);
}
