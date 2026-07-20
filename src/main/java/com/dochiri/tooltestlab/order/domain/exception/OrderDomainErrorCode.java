package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum OrderDomainErrorCode implements ErrorCode {

    BAD_ORDER_STATUS("BAD_ORDER_STATUS", ErrorKind.INVALID_STATE, "현재 주문 상태에서는 처리할 수 없습니다."),
    INVALID_ORDER_ID("ORDER_ID_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 주문 ID입니다."),
    ORDER_ID_REQUIRED("ORDER_ID_REQUIRED", ErrorKind.INVALID_INPUT, "주문 ID는 필수입니다."),
    INVALID_QUANTITY("QUANTITY_INVALID", ErrorKind.INVALID_INPUT, "수량은 1 이상이어야 합니다."),
    QUANTITY_REQUIRED("QUANTITY_REQUIRED", ErrorKind.INVALID_INPUT, "수량은 필수입니다."),
    BAD_SHIPPING_ADDR("SHIPPING_ADDRESS_INVALID", ErrorKind.INVALID_INPUT, "배송지 정보가 올바르지 않습니다."),
    SHIPPING_ADDRESS_REQUIRED("SHIPPING_ADDRESS_REQUIRED", ErrorKind.INVALID_INPUT, "배송지 정보는 필수입니다."),
    BAD_ORDER_LINES("ORDER_LINES_INVALID", ErrorKind.INVALID_INPUT, "주문 상품 목록이 올바르지 않습니다."),
    ORDER_LINES_REQUIRED("ORDER_LINES_REQUIRED", ErrorKind.INVALID_INPUT, "주문 상품 목록은 필수입니다."),
    ORDER_STATUS_REQUIRED("ORDER_STATUS_REQUIRED", ErrorKind.INVALID_INPUT, "주문 상태는 필수입니다."),
    TOTAL_AMOUNT_REQUIRED("TOTAL_AMOUNT_REQUIRED", ErrorKind.INVALID_INPUT, "총 금액은 필수입니다."),
    PRODUCT_ID_REQUIRED("PRODUCT_ID_REQUIRED", ErrorKind.INVALID_INPUT, "상품 ID는 필수입니다."),
    PRODUCT_NAME_REQUIRED("PRODUCT_NAME_REQUIRED", ErrorKind.INVALID_INPUT, "상품명은 필수입니다."),
    UNIT_PRICE_REQUIRED("UNIT_PRICE_REQUIRED", ErrorKind.INVALID_INPUT, "단가는 필수입니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    OrderDomainErrorCode(final String code, final ErrorKind kind, final String detail) {
        this.code = code;
        this.kind = kind;
        this.detail = detail;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorKind kind() {
        return kind;
    }

    @Override
    public String detail() {
        return detail;
    }
}
