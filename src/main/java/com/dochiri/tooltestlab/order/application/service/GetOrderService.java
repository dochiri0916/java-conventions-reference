package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.result.GetOrderResult;
import com.dochiri.tooltestlab.order.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetOrderService implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public GetOrderService(final OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public GetOrderResult getOrder(final String publicId) {
        final Order order = orderRepositoryPort.findByPublicId(publicId)
                .orElseThrow(() -> new OrderNotFoundException(publicId));

        final var items = order.items().values().stream()
                .map(i -> new GetOrderResult.Item(i.productId().value(), i.productName().value(), i.quantity().value(), i.unitPrice().amount(), i.totalPrice().amount()))
                .toList();

        return new GetOrderResult(
                order.id().value(), order.memberId().value(), order.status().name(), order.totalAmount().amount(),
                order.shippingAddress().recipientName(), order.shippingAddress().phone(),
                order.shippingAddress().address(), order.shippingAddress().zipCode(),
                items);
    }
}
