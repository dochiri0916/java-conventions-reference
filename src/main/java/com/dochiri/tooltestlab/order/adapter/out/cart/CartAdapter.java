package com.dochiri.tooltestlab.order.adapter.out.cart;

import com.dochiri.tooltestlab.cart.application.port.in.ClearCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.GetCartUseCase;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.application.port.out.CartPort;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class CartAdapter implements CartPort {

    private final GetCartUseCase getCartUseCase;
    private final ClearCartUseCase clearCartUseCase;

    @Override
    public List<CartLine> findLines(final MemberReferenceId memberId) {
        return getCartUseCase.getCart(memberId.value()).items().stream()
                .map(item -> new CartLine(
                        ProductReferenceId.parse(item.productPublicId()),
                        ProductName.of(item.productName()),
                        Quantity.of(item.quantity()),
                        Money.of(item.unitPrice())))
                .toList();
    }

    @Override
    public void clear(final MemberReferenceId memberId) {
        clearCartUseCase.clear(memberId.value());
    }
}
