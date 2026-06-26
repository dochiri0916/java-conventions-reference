package com.dochiri.tooltestlab.product.application.port.in;

import com.dochiri.tooltestlab.product.application.command.RegisterProductCommand;
import com.dochiri.tooltestlab.product.application.result.RegisterProductResult;

/**
 * 상품 등록 UseCase.
 */
public interface RegisterProductUseCase {
    RegisterProductResult register(RegisterProductCommand command);
}
