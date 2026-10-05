package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.application.port.in.query.GetOrderQuery;
import com.dochiri.tooltestlab.order.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OrderOwnershipServiceTest {

    @Test
    @DisplayName("다른 회원의 주문 취소는 저장과 이벤트 발행 전에 거부한다")
    void rejects_cancellation_before_updating_or_publishing() {
        // given
        final Order order = order();
        final OrderRepositoryPort orders = mock(OrderRepositoryPort.class);
        final IntegrationEventPort events = mock(IntegrationEventPort.class);
        final CancelOrderService service = new CancelOrderService(orders, events);
        when(orders.findByOrderIdForUpdate(order.id())).thenReturn(Optional.of(order));

        // when & then
        assertThrows(NotOrderOwnerException.class, () -> service.cancel(
                "01900000-0000-7000-8000-000000000004", order.id().value()));
        verify(orders, never()).update(any());
        verifyNoInteractions(events);
        assertEquals(OrderStatus.PENDING, order.status());
    }

    @Test
    @DisplayName("다른 회원의 주문 상세는 도메인 소유자 검증에서 거부한다")
    void rejects_reading_another_members_order() {
        // given
        final Order order = order();
        final OrderRepositoryPort orders = mock(OrderRepositoryPort.class);
        final GetOrderService service = new GetOrderService(orders);
        when(orders.findByOrderId(order.id())).thenReturn(Optional.of(order));

        // when & then
        assertThrows(NotOrderOwnerException.class, () -> service.getOrder(new GetOrderQuery(
                "01900000-0000-7000-8000-000000000004", order.id().value())));
    }

    private static Order order() {
        return Order.create(MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"),
                CartReferenceId.parse("01900000-0000-7000-8000-000000000003"), 1,
                OrderLines.of(OrderLine.of(ProductReferenceId.parse("01900000-0000-7000-8000-000000000002"),
                        ProductName.of("상품"), Quantity.of(2), Money.of(100))),
                ShippingAddress.of(RecipientName.of("홍길동"), Phone.of("010-1234-5678"),
                        StreetAddress.of("서울시 주소"), ZipCode.of("12345")));
    }
}
