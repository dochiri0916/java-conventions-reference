package com.dochiri.tooltestlab.product.application.exception;

import com.dochiri.tooltestlab.global.error.BusinessException;

/**
 * 상품이 존재하지 않을 때 발생하는 예외.
 * {@link GlobalExceptionHandler#handleBusinessException}에 의해 HTTP 404로 변환된다.
 */
public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(String productPublicId) {
        super(
                ProductErrorCode.PRODUCT_NOT_FOUND,
                "해당 상품을 찾을 수 없습니다. productPublicId=" + productPublicId
        );
    }

}
