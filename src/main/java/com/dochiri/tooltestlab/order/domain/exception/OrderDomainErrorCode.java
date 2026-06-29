package com.dochiri.tooltestlab.order.domain.exception;

public enum OrderDomainErrorCode {

    BAD_ORDER_STATUS("현재 주문 상태에서는 처리할 수 없습니다."),
    INVALID_ORDER_ID("주문 ID는 빈 값일 수 없습니다."),
    INVALID_QUANTITY("수량은 1 이상이어야 합니다."),
    BAD_SHIPPING_ADDR("배송지 정보가 올바르지 않습니다."),
    BAD_ORDER_LINES("주문 상품 목록이 올바르지 않습니다.");

    private final String message;

    OrderDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
