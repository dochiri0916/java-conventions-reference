package com.dochiri.tooltestlab.catalog.product.application.port.in;

import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;

public interface RegisterProductUseCase {

    RegisterProductResult register(RegisterProductCommand command);

}
