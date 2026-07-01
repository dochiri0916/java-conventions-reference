package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.GetOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class GetOrderService implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetOrderResult getOrder(final String memberPublicId, final String publicId) {
        final Order order = orderRepositoryPort.findByPublicId(publicId)
                .orElseThrow(() -> OrderNotFoundException.orderNotFound(publicId));

        if (!order.memberId().value().equals(memberPublicId)) {
            throw NotOrderOwnerException.notOrderOwner();
        }

        final var items = order.items().values().stream()
                .map(i -> new GetOrderResult.Item(i.productId().value(), i.productName().value(), i.quantity().value(), i.unitPrice().amount(), i.totalPrice().amount()))
                .toList();

        return new GetOrderResult(
                order.id().value(), order.memberId().value(), order.status().name(), order.totalAmount().amount(),
                order.shippingAddress().recipientName().value(), order.shippingAddress().phone().value(),
                order.shippingAddress().address().value(), order.shippingAddress().zipCode().value(),
                items);
    }
}
