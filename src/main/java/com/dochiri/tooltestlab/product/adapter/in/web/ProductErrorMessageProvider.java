package com.dochiri.tooltestlab.product.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorMessage;
import com.dochiri.tooltestlab.global.error.ApiErrorMessageProvider;
import com.dochiri.tooltestlab.product.application.exception.ProductErrorCode;
import com.dochiri.tooltestlab.product.domain.exception.ProductDomainErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(ProductDomainErrorCode.LOW_STOCK, "재고 부족", "재고가 부족합니다."),
            message(ProductDomainErrorCode.BAD_PRODUCT_ID, "상품 ID 오류", "상품 ID는 공백일 수 없습니다."),
            message(ProductDomainErrorCode.PRODUCT_ID_REQUIRED, "상품 ID 누락", "상품 ID는 필수입니다."),
            message(ProductDomainErrorCode.BAD_PRODUCT_NAME, "상품명 오류", "상품명은 공백일 수 없습니다."),
            message(ProductDomainErrorCode.PRODUCT_NAME_REQUIRED, "상품명 누락", "상품명은 필수입니다."),
            message(ProductDomainErrorCode.INVALID_STOCK, "재고 오류", "재고 값이 올바르지 않습니다."),
            message(ProductDomainErrorCode.STOCK_REQUIRED, "재고 누락", "재고는 필수입니다."),
            message(ProductDomainErrorCode.INVALID_PRODUCT, "상품 오류", "상품 정보가 올바르지 않습니다."),
            message(ProductDomainErrorCode.PRODUCT_REQUIRED, "상품 누락", "상품 정보는 필수입니다."),
            message(ProductDomainErrorCode.MONEY_NEGATIVE, "금액 오류", "금액은 0 이상이어야 합니다."),
            message(ProductDomainErrorCode.MONEY_ADD_NULL, "금액 누락", "더할 금액은 필수입니다."),
            message(ProductDomainErrorCode.MONEY_REQUIRED, "금액 누락", "금액은 필수입니다."),
            message(ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE, "수량 오류", "수량은 0 이상이어야 합니다."),
            message(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE, "최대 개수 오류", "최대 개수는 0보다 커야 합니다."),
            message(ProductErrorCode.PRODUCT_NOT_FOUND, "상품 없음", "상품을 찾을 수 없습니다."),
            message(ProductErrorCode.INVALID_INPUT, "입력값 오류", "입력값이 올바르지 않습니다.")
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
