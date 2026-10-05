package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class InvalidProductNameException extends ProductDomainException {

    private final String productName;

    public InvalidProductNameException(final String productName) {
        super("유효하지 않은 상품명입니다.");
        this.productName = productName;
    }

    public String productName() {
        return productName;
    }

}
