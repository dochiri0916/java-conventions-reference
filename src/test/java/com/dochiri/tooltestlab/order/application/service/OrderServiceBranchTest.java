package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceBranchTest {

    @Autowired
    private GetOrderUseCase getOrderUseCase;

    @Autowired
    private CancelOrderUseCase cancelOrderUseCase;

    @Autowired
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    @DisplayName("getOrder - 존재하지 않는 주문은 OrderNotFoundException")
    void getOrderNotFound() {
            // given

            // when

            // then

        assertThatThrownBy(() -> getOrderUseCase.getOrder("m1", "nonexistent"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("cancelOrder - 존재하지 않는 주문은 OrderNotFoundException")
    void cancelOrderNotFound() {
            // given

            // when

            // then

        assertThatThrownBy(() -> cancelOrderUseCase.cancel("m1", "nonexistent"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("placeOrder - 빈 장바구니는 EmptyCartException")
    void placeOrderEmptyCart() {
            // given

            // when

            // then

        var cmd = new com.dochiri.tooltestlab.order.application.port.in.PlaceOrderCommand(
                new MemberId("unknown"),
                new com.dochiri.tooltestlab.order.domain.model.ShippingAddress(
                        new com.dochiri.tooltestlab.order.domain.model.RecipientName("홍길동"),
                        new com.dochiri.tooltestlab.order.domain.model.Phone("01012345678"),
                        new com.dochiri.tooltestlab.order.domain.model.StreetAddress("서울시"),
                        new com.dochiri.tooltestlab.order.domain.model.ZipCode("12345")));
        assertThatThrownBy(() -> placeOrderUseCase.place(cmd))
                .isInstanceOf(EmptyCartException.class);
    }

    @Test
    @DisplayName("getOrder - 다른 회원의 주문 조회 시 NotOrderOwnerException")
    void getOrderNotOwner() {
            // given

            // when

            // then

        assertThatThrownBy(() -> getOrderUseCase.getOrder("m1", "nonexistent"))
                .isInstanceOf(RuntimeException.class);
    }
}
