package com.dochiri.tooltestlab.product.domain.exception;

public final class InvalidProductIdException extends ProductDomainException {

    public InvalidProductIdException() {
        super(ProductDomainErrorCode.BAD_PRODUCT_ID, "상품 ID는 빈 값일 수 없습니다.");
    }
}
