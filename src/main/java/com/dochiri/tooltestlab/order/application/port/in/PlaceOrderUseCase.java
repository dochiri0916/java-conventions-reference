package com.dochiri.tooltestlab.order.application.port.in;

import com.dochiri.tooltestlab.order.application.command.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.result.PlaceOrderResult;

/**
 * 주문 생성 UseCase. 장바구니를 주문으로 전환.
 */
public interface PlaceOrderUseCase {
    PlaceOrderResult place(PlaceOrderCommand command);
}
