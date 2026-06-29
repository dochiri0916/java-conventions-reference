package com.dochiri.tooltestlab.product.domain.exception;

public final class InvalidProductNameException extends ProductDomainException {

    public InvalidProductNameException() {
        super(ProductDomainErrorCode.BAD_PRODUCT_NAME, "상품명은 빈 값일 수 없습니다.");
    }
}
