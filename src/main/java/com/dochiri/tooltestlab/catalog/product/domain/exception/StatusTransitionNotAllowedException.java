package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;

public final class StatusTransitionNotAllowedException extends RuntimeException {

    private final ProductStatus currentStatus;
    private final ProductStatus targetStatus;

    public StatusTransitionNotAllowedException(final ProductStatus currentStatus, final ProductStatus targetStatus) {
        super("허용되지 않는 상품 상태 변경입니다.");
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public ProductStatus currentStatus() {
        return currentStatus;
    }

    public ProductStatus targetStatus() {
        return targetStatus;
    }

}
