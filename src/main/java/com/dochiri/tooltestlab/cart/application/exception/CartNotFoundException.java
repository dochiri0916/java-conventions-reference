package com.dochiri.tooltestlab.cart.application.exception;

import com.dochiri.tooltestlab.global.error.BusinessException;

/**
 * 장바구니가 존재하지 않을 때 발생하는 예외.
 * {@link GlobalExceptionHandler#handleBusinessException}에 의해 HTTP 404로 변환된다.
 */
public class CartNotFoundException extends BusinessException {

    public CartNotFoundException(String memberPublicId) {
        super(
                CartErrorCode.CART_NOT_FOUND,
                "해당 회원의 장바구니를 찾을 수 없습니다. memberPublicId=" + memberPublicId
        );
    }

}
