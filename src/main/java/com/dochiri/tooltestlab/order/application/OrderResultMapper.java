package com.dochiri.tooltestlab.order.application;

import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResultItems;
import com.dochiri.tooltestlab.order.application.port.in.result.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.port.in.result.PlaceOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.result.PlaceOrderResultItems;
import com.dochiri.tooltestlab.order.application.port.out.Orders;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.OrderStatus;
import com.dochiri.tooltestlab.order.domain.vo.Phone;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.RecipientName;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.vo.StreetAddress;
import com.dochiri.tooltestlab.order.domain.vo.ZipCode;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;

import java.util.List;

public final class OrderResultMapper {
    private OrderResultMapper() {
    }

    public static PlaceOrderResult toPlaceOrderResult(final Order order) {
        final OrderId id = order.id();
        final OrderStatus status = order.status();
        final Money total = order.totalAmount();
        final ShippingAddress shipping = order.shippingAddress();
        final RecipientName recipientName = shipping.recipientName();
        final Phone phone = shipping.phone();
        final StreetAddress address = shipping.address();
        final ZipCode zipCode = shipping.zipCode();
        return new PlaceOrderResult(
                id.value(),
                status.name(),
                total.amount(),
                recipientName.value(),
                phone.value(),
                address.value(),
                zipCode.value(),
                new PlaceOrderResultItems(placeItems(order.items())));
    }

    public static GetOrderResult toGetOrderResult(final Order order) {
        final OrderId id = order.id();
        final MemberReferenceId memberId = order.memberId();
        final OrderStatus status = order.status();
        final Money total = order.totalAmount();
        final ShippingAddress shipping = order.shippingAddress();
        final RecipientName recipientName = shipping.recipientName();
        final Phone phone = shipping.phone();
        final StreetAddress address = shipping.address();
        final ZipCode zipCode = shipping.zipCode();
        return new GetOrderResult(
                id.value(),
                memberId.value(),
                status.name(),
                total.amount(),
                recipientName.value(),
                phone.value(),
                address.value(),
                zipCode.value(),
                new GetOrderResultItems(getItems(order.items())));
    }

    public static ListOrdersResult toListOrdersResult(final Orders found) {
        final List<ListOrdersResult.OrderSummary> summaries = found.values().stream()
                .map(OrderResultMapper::toSummary)
                .toList();
        return new ListOrdersResult(summaries);
    }

    private static ListOrdersResult.OrderSummary toSummary(final Order order) {
        final OrderId id = order.id();
        final OrderStatus status = order.status();
        final Money total = order.totalAmount();
        final OrderLines items = order.items();
        return new ListOrdersResult.OrderSummary(
                id.value(),
                status.name(),
                total.amount(),
                items.size());
    }

    private static List<PlaceOrderResult.OrderItemResult> placeItems(final OrderLines lines) {
        return lines.values().stream()
                .map(OrderResultMapper::toPlaceItem)
                .toList();
    }

    private static PlaceOrderResult.OrderItemResult toPlaceItem(final OrderLine line) {
        final ProductReferenceId productId = line.productId();
        final ProductName productName = line.productName();
        final Quantity quantity = line.quantity();
        final Money unitPrice = line.unitPrice();
        return new PlaceOrderResult.OrderItemResult(
                productId.value(),
                productName.value(),
                quantity.value(),
                unitPrice.amount());
    }

    private static List<GetOrderResult.Item> getItems(final OrderLines lines) {
        return lines.values().stream()
                .map(OrderResultMapper::toGetItem)
                .toList();
    }

    private static GetOrderResult.Item toGetItem(final OrderLine line) {
        final ProductReferenceId productId = line.productId();
        final ProductName productName = line.productName();
        final Quantity quantity = line.quantity();
        final Money unitPrice = line.unitPrice();
        final Money totalPrice = line.totalPrice();
        return new GetOrderResult.Item(
                productId.value(),
                productName.value(),
                quantity.value(),
                unitPrice.amount(),
                totalPrice.amount());
    }
}
