package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.CartPort;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.ProductStockPort;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.Phone;
import com.dochiri.tooltestlab.order.domain.vo.RecipientName;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.vo.StreetAddress;
import com.dochiri.tooltestlab.order.domain.vo.ZipCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final CartPort cartPort;
    private final ProductStockPort productStockPort;

    @Override
    @Transactional
    public PlaceOrderResult place(final PlaceOrderCommand command) {
        final MemberReferenceId memberId = MemberReferenceId.parse(command.memberPublicId());
        final ShippingAddress shippingAddress = ShippingAddress.of(
                RecipientName.of(command.recipientName()), Phone.of(command.phone()),
                StreetAddress.of(command.address()), ZipCode.of(command.zipCode()));

        final List<CartPort.CartLine> cartLines = cartPort.findLines(memberId);
        if (cartLines.isEmpty()) {
            throw new EmptyCartException();
        }

        // 데드락 방지를 위해 상품 ID 순으로 재고를 잠그고 차감한다. 재고 부족 시 트랜잭션 전체가 롤백된다.
        cartLines.stream()
                .sorted(Comparator.comparing(line -> line.productId().value()))
                .forEach(line -> productStockPort.decrease(line.productId(), line.quantity()));

        final List<OrderLine> orderLines = cartLines.stream()
                .map(line -> OrderLine.of(line.productId(), line.productName(), line.quantity(), line.unitPrice()))
                .toList();

        final Order saved = orderRepositoryPort.create(
                Order.create(memberId, OrderLines.of(orderLines), shippingAddress));
        cartPort.clear(memberId);

        final var resultItems = saved.items().values().stream()
                .map(i -> new PlaceOrderResult.OrderItemResult(
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
        return new PlaceOrderResult(
                saved.id().value(),
                saved.status().name(),
                saved.totalAmount().amount(),
                resultItems);
    }
}
