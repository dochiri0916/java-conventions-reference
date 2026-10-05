package com.dochiri.tooltestlab.catalog.product.application.port.in.usecase;

import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;

@FunctionalInterface
public interface RegisterProductUseCase {

    RegisterProductResult register(RegisterProductCommand command);

}
