package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.application.exception.CartErrorCode;
import com.dochiri.tooltestlab.cart.domain.exception.CartDomainErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorMessage;
import com.dochiri.tooltestlab.global.error.ApiErrorMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CartErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(CartDomainErrorCode.INVALID_CART_ID, "장바구니 ID 오류", "장바구니 ID는 공백일 수 없습니다."),
            message(CartDomainErrorCode.CART_ID_REQUIRED, "장바구니 ID 누락", "장바구니 ID는 필수입니다."),
            message(CartDomainErrorCode.INVALID_CART_ITEM, "장바구니 상품 오류", "장바구니 상품 정보가 올바르지 않습니다."),
            message(CartDomainErrorCode.CART_ITEM_REQUIRED, "장바구니 상품 누락", "장바구니 상품은 필수입니다."),
            message(CartDomainErrorCode.INVALID_CART, "장바구니 오류", "장바구니 정보가 올바르지 않습니다."),
            message(CartDomainErrorCode.CART_REQUIRED, "장바구니 누락", "장바구니 정보는 필수입니다."),
            message(CartDomainErrorCode.TOO_MANY_ITEMS, "장바구니 상품 수 초과", "장바구니 상품은 최대 100개까지 담을 수 있습니다."),
            message(CartErrorCode.CART_NOT_FOUND, "장바구니 없음", "장바구니를 찾을 수 없습니다."),
            message(CartErrorCode.CART_ITEM_NOT_FOUND, "장바구니 상품 없음", "장바구니 상품을 찾을 수 없습니다."),
            message(CartErrorCode.NOT_CART_OWNER, "장바구니 권한 없음", "장바구니를 변경할 권한이 없습니다."),
            message(CartErrorCode.INVALID_INPUT, "입력값 오류", "입력값이 올바르지 않습니다.")
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
