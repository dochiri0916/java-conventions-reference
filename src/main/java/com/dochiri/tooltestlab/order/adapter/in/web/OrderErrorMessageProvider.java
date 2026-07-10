package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorMessage;
import com.dochiri.tooltestlab.global.error.ApiErrorMessageProvider;
import com.dochiri.tooltestlab.order.application.exception.OrderApplicationErrorCode;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(OrderDomainErrorCode.BAD_ORDER_STATUS, "주문 상태 오류", "현재 주문 상태에서는 처리할 수 없습니다."),
            message(OrderDomainErrorCode.INVALID_ORDER_ID, "주문 ID 오류", "주문 ID는 공백일 수 없습니다."),
            message(OrderDomainErrorCode.ORDER_ID_REQUIRED, "주문 ID 누락", "주문 ID는 필수입니다."),
            message(OrderDomainErrorCode.INVALID_QUANTITY, "수량 오류", "수량은 1 이상이어야 합니다."),
            message(OrderDomainErrorCode.QUANTITY_REQUIRED, "수량 누락", "수량은 필수입니다."),
            message(OrderDomainErrorCode.BAD_SHIPPING_ADDR, "배송지 오류", "배송지 정보가 올바르지 않습니다."),
            message(OrderDomainErrorCode.SHIPPING_ADDRESS_REQUIRED, "배송지 누락", "배송지 정보는 필수입니다."),
            message(OrderDomainErrorCode.BAD_ORDER_LINES, "주문 상품 오류", "주문 상품 목록이 올바르지 않습니다."),
            message(OrderDomainErrorCode.ORDER_LINES_REQUIRED, "주문 상품 누락", "주문 상품 목록은 필수입니다."),
            message(OrderDomainErrorCode.ORDER_STATUS_REQUIRED, "주문 상태 누락", "주문 상태는 필수입니다."),
            message(OrderDomainErrorCode.TOTAL_AMOUNT_REQUIRED, "총 금액 누락", "총 금액은 필수입니다."),
            message(OrderDomainErrorCode.PRODUCT_ID_REQUIRED, "상품 ID 누락", "상품 ID는 필수입니다."),
            message(OrderDomainErrorCode.PRODUCT_NAME_REQUIRED, "상품명 누락", "상품명은 필수입니다."),
            message(OrderDomainErrorCode.UNIT_PRICE_REQUIRED, "단가 누락", "단가는 필수입니다."),
            message(OrderApplicationErrorCode.ORDER_NOT_FOUND, "주문 없음", "주문을 찾을 수 없습니다."),
            message(OrderApplicationErrorCode.NOT_ORDER_OWNER, "주문 권한 없음", "주문을 변경할 권한이 없습니다."),
            message(OrderApplicationErrorCode.EMPTY_CART, "빈 장바구니", "장바구니에 담긴 상품이 없습니다.")
    );

    @Override
    public Map<String, ApiErrorMessage> errorMessages() {
        return Map.copyOf(ERROR_MESSAGES);
    }

    private static Map.Entry<String, ApiErrorMessage> message(
            final Enum<?> errorCode,
            final String title,
            final String detail) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiErrorMessage(title, detail));
    }
}
