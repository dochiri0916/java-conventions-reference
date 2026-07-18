package com.dochiri.tooltestlab.catalog.product.application.port.in;

import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;

public interface RegisterProductUseCase {

    ProductId register(RegisterProductCommand command);

}
