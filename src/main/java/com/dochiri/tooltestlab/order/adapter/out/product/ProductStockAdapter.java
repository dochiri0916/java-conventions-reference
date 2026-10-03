package com.dochiri.tooltestlab.order.adapter.out.product;

import com.dochiri.tooltestlab.catalog.product.application.port.in.DecreaseProductStockUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.IncreaseProductStockUseCase;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.application.port.out.ProductStockPort;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ProductStockAdapter implements ProductStockPort {

    private final DecreaseProductStockUseCase decreaseProductStockUseCase;
    private final IncreaseProductStockUseCase increaseProductStockUseCase;

    @Override
    public void decrease(final ProductReferenceId productId, final Quantity quantity) {
        decreaseProductStockUseCase.decrease(productId.value(), quantity.value());
    }

    @Override
    public void increase(final ProductReferenceId productId, final Quantity quantity) {
        increaseProductStockUseCase.increase(productId.value(), quantity.value());
    }
}
