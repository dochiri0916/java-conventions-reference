package com.dochiri.tooltestlab.order.application;

import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.result.PlaceOrderResult;
import com.dochiri.tooltestlab.order.domain.Order;

import java.util.List;

public final class OrderResultMapper {
    private OrderResultMapper() {
    }

    public static PlaceOrderResult toPlaceOrderResult(final Order order) {
        final List<PlaceOrderResult.OrderItemResult> items = order.items().values().stream()
                .map(i -> new PlaceOrderResult.OrderItemResult(
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount()))
                .toList();
        return new PlaceOrderResult(
                order.id().value(),
                order.status().name(),
                order.totalAmount().amount(),
                items);
    }

    public static GetOrderResult toGetOrderResult(final Order order) {
        final List<GetOrderResult.Item> items = order.items().values().stream()
                .map(i -> new GetOrderResult.Item(
                        i.productId().value(),
                        i.productName().value(),
                        i.quantity().value(),
                        i.unitPrice().amount(),
                        i.totalPrice().amount()))
                .toList();
        return new GetOrderResult(
                order.id().value(), order.memberId().value(), order.status().name(), order.totalAmount().amount(),
                order.shippingAddress().recipientName().value(), order.shippingAddress().phone().value(),
                order.shippingAddress().address().value(), order.shippingAddress().zipCode().value(),
                items);
    }
}
